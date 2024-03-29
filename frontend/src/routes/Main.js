import '@fontsource/roboto';

import { createTheme, ThemeProvider } from '@mui/material';
import React, { useContext, useEffect } from 'react'
import { BrowserRouter as Router, Navigate, Route, Routes } from 'react-router-dom';

import { AuthContext, AuthProvider } from '../contexts/AuthProvider'
import { UserProvider } from '../contexts/UserProvider';
import { App } from '../pages/App'
import Home from '../pages/Home'
import Login from '../pages/Login'
import NotFound from '../pages/NotFound';
import Perfil from '../pages/Perfil';
import Register from '../pages/Register'
import { NotificationProvider } from '../contexts/NotificationProvider'
import RecoverPassword from '../pages/RecoverPassword';
import ValidateRecoverPassword from '../pages/ValidateRecoverPassword';

const theme = createTheme({
  typography: {
    fontFamily: 'Roboto, sans-serif',
  },
});

const PrivateRoute = ({ children }) => {
  const { isAuthenticated, authLoading } = useContext(AuthContext);

  useEffect(() => { }, [authLoading])

  if (authLoading) return null;

  return isAuthenticated ? (children) :
    (<Navigate to='/login' replace />);
}

const Main = () => {
  return (
    <ThemeProvider theme={theme}>
      <NotificationProvider>
        <AuthProvider>
          <UserProvider>
            <Router>
              <Routes>
                <Route path='/chat'
                  element={<PrivateRoute><App /></PrivateRoute>} />
                <Route path='/perfil'
                  element={<PrivateRoute><Perfil /></PrivateRoute>} />
                <Route path='/' element={<Home />} />
                <Route path='/login' element={<Login />} />
                <Route path='/recover-password' element={<RecoverPassword />} />
                <Route path='/validate/recover-password/:passwordCode' element={<ValidateRecoverPassword />} />
                <Route path='/register' element={<Register />} />
                <Route path='*' element={<NotFound />} />
              </Routes>
            </Router>
          </UserProvider>
        </AuthProvider>
      </NotificationProvider>
    </ThemeProvider>
  )
}

export default Main