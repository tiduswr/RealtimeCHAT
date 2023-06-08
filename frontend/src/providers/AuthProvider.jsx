import React, { createContext, useState, useEffect } from 'react';
import { Api, Auth } from '../api';
import LoginAlert from '../component/LoginAlert';

const AuthContext = createContext();

const AuthProvider = ({ children }) => {
    const [ isAuthenticated, setIsAuthenticated ] = useState(false);
    const [ alert, setAlert ] = useState({title: 'Erro ao tentar logar', message: '', type: 'error', show: false});
    const [ loading, setLoading ] = useState(true);

    useEffect(() => {
        const accessToken = localStorage.getItem('access_token');

        if(accessToken){
            Api.defaults.headers.Authorization = `Bearer ${JSON.parse(accessToken).jwtToken}`;
            setIsAuthenticated(true);
        }

        setLoading(false);
    }, [])

    useEffect(() => {
        if (alert.show) {
            setTimeout(() => {
                setAlert(prevAlert => {
                    return { ...prevAlert, show: false };
                });
            }, 3000);
        }
    }, [alert]);
    

    const login = async (username, password) => {
        if(username !== '' && password !== ''){
            await Auth.post('/auth', {
                userName: username,
                password: password
            }).then(res => {
                const { refreshToken, token } = res.data;
                localStorage.setItem('refresh_token', JSON.stringify(refreshToken));
                localStorage.setItem('access_token', JSON.stringify(token));
                Api.defaults.headers.Authorization = `Bearer ${token}`;
                setIsAuthenticated(true);
            }).catch(ex => {
                setAlert(prevAlert => {
                    return { ...prevAlert, show: true, message: 'Usuário não encontrado!', title: 'Erro ao tentar logar' };
                });
            })
        }else{
            setAlert(prevAlert => {
                return { ...prevAlert, show: true, message: 'Preencha o login e Senha', title: 'Erro no formulário' };
            });
        }
    };
      

    const logout = async () => {

        const refreshToken = localStorage.getItem('refresh_token');

        if(refreshToken && isAuthenticated){
            await Auth.post('/quit', {
                refreshToken : JSON.parse(refreshToken).jwtToken
            }).then(res => {
                localStorage.removeItem('refresh_token');
                localStorage.removeItem('access_token');
                Api.defaults.headers.Authorization = undefined;
                setIsAuthenticated(false);
            }).catch(ex => {
                console.log(ex);
                setAlert(prevAlert => {
                    return {...prevAlert, show: true, message: 'Logout deu erro', title: 'Não foi deslogar do app!'};
                })
            });
        }else{
            setAlert(prevAlert => {
                return {...prevAlert, show: true, message: 'Não foi possivel deslogar', title: 'Você não está logado!'};
            })
        }

    };

    if(loading){
        return null;
    }

    return (
        <AuthContext.Provider value={{ isAuthenticated, login, logout, loading }}>
            {children}
            {alert.show && 
                <LoginAlert message={alert.message} type={alert.type} title={alert.title}/>
            }
        </AuthContext.Provider>
    );

}

export { AuthContext, AuthProvider };