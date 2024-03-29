import React, { createContext, useState, useEffect, useContext } from 'react';
import { Auth, handleTokenRefreshRequest } from '../api';
import moment from 'moment';
import { NotificationContext } from './NotificationProvider';
import { tryGetErrorMessage } from '../errorParser';

const AuthContext = createContext();

const AuthProvider = ({ children }) => {
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [authLoading, setAuthLoading] = useState(true);
    const { setAlert } = useContext(NotificationContext);

    useEffect(() => {
        setAuthLoading(true);

        const checkTokenValidity = async () => {
            const accessTokenString = localStorage.getItem('access_token');
            if (accessTokenString) {
                let accessTk = JSON.parse(accessTokenString);

                const isExpired = moment(accessTk.expiration).isBefore(moment());

                if (isExpired) accessTk = await handleTokenRefreshRequest();
                if (accessTk) setIsAuthenticated(true);
            }
            setAuthLoading(false);
        };

        checkTokenValidity();
    }, []);

    const login = async (username, password) => {
        if (username !== '' && password !== '') {
            await Auth.post('/auth', {
                userName: username,
                password: password
            }).then(res => {
                const { refreshToken, token } = res.data;
                if (refreshToken && token) {
                    localStorage.setItem('refresh_token', JSON.stringify(refreshToken));
                    localStorage.setItem('access_token', JSON.stringify(token));
                    setIsAuthenticated(true);
                }
            }).catch(ex => {
                if ([400, 403, 404, 401].includes(ex.response?.status)) {
                    setAlert({ type: 'error', message: 'Credenciais inválidas!' });
                } else {
                    setAlert({type: 'error', message: tryGetErrorMessage(ex, 'O servidor não respondeu a solicitação.')});
                }
            })
        } else {
            setAlert({ type: 'error', message: 'Preencha o login e Senha' });
        }
    };

    const logout = async () => {
        const refreshToken = localStorage.getItem('refresh_token');

        if (refreshToken) {
            await Auth.post('/quit', {
                refreshToken: JSON.parse(refreshToken).jwtToken
            });
        }

        localStorage.removeItem('refresh_token');
        localStorage.removeItem('access_token');
        setIsAuthenticated(false);
    };

    return (
        <AuthContext.Provider value={{ isAuthenticated, login, logout, authLoading }}>
            {children}
        </AuthContext.Provider>
    );

}

export { AuthContext, AuthProvider };