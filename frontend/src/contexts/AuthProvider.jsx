import React, { createContext, useState, useEffect, useCallback } from 'react';
import { Auth } from '../api';
import AuthAlert from '../component/AuthAlert';
import moment from 'moment';

const AuthContext = createContext();

const AuthProvider = ({ children }) => {
    const [ isAuthenticated, setIsAuthenticated ] = useState(false);
    const [ alert, setAlert ] = useState({title: 'Erro ao tentar logar', message: '', type: 'error', show: false});
    const [ authLoading, setAuthLoading ] = useState(true);

    const refreshToken = useCallback(async () => {
        let oldRefreshToken = localStorage.getItem('refresh_token');
        oldRefreshToken = JSON.parse(oldRefreshToken).jwtToken;

        if (oldRefreshToken) {
            try {
                const res = await Auth.post(`/refresh_token`, { 'refreshToken': oldRefreshToken });

                if (res.status === 200) {
                    const { refreshToken, token } = res.data;

                    if(refreshToken?.jwtToken && token?.jwtToken){
                        localStorage.setItem('refresh_token', JSON.stringify(refreshToken));
                        localStorage.setItem('access_token', JSON.stringify(token));
                    }

                    return token;
                }else{
                    await logout();
                }
            } catch (error) {
                await logout();
            }
            return undefined;
        }
    }, []);

    useEffect(() => {
        setAuthLoading(true);

        const checkTokenValidity = async () => {
            const accessTokenString = localStorage.getItem('access_token');
            if(accessTokenString){
                let accessTk = JSON.parse(accessTokenString);
        
                const isExpired = moment(accessTk.expiration).isBefore(moment());
            
                if (isExpired) accessTk = await refreshToken();
                if (accessTk) setIsAuthenticated(true);
            }
            setAuthLoading(false);
        };
        
        checkTokenValidity();
    }, [refreshToken]);

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
                if(refreshToken && token){
                    localStorage.setItem('refresh_token', JSON.stringify(refreshToken));
                    localStorage.setItem('access_token', JSON.stringify(token));
                    setIsAuthenticated(true);
                    console.log('Refreshed token')
                }
            }).catch(ex => {
                setAlert(prevAlert => {
                    return { ...prevAlert, show: true, type: 'error', message: 'Usuário não encontrado!', title: 'Erro ao tentar logar' };
                });
            })
        }else{
            setAlert(prevAlert => {
                return { ...prevAlert, show: true, type: 'error', message: 'Preencha o login e Senha', title: 'Erro no formulário' };
            });
        }
    };
      

    const logout = async () => {
        const refreshToken = localStorage.getItem('refresh_token');

        if(refreshToken){
            await Auth.post('/quit', {
                refreshToken : JSON.parse(refreshToken).jwtToken
            });
        }

        localStorage.removeItem('refresh_token');
        localStorage.removeItem('access_token');
        setIsAuthenticated(false);
    };

    return (
        <AuthContext.Provider value={{ isAuthenticated, login, logout, setAlert, authLoading }}>
            {children}
            {alert.show && 
                <AuthAlert message={alert.message} type={alert.type} title={alert.title}/>
            }
        </AuthContext.Provider>
    );

}

export { AuthContext, AuthProvider };