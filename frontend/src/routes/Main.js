import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import React, { useContext } from 'react'
import Home from '../pages/Home'
import { App } from '../pages/App'
import NotFound from '../pages/NotFound';
import Login from '../pages/Login'
import Register from '../pages/Register'
import { createTheme, ThemeProvider } from '@mui/material';
import { AuthContext, AuthProvider } from '../contexts/AuthProvider'
import '@fontsource/roboto';
import Perfil from '../pages/Perfil';
import { UserProvider } from '../contexts/UserProvider';

const theme = createTheme({
  typography: {
    fontFamily: 'Roboto, sans-serif',
  },
});

const PrivateRoute = ({ children }) => {
  const { isAuthenticated } = useContext(AuthContext);

  return isAuthenticated ? (
    children
  ) : (
    <Navigate to='/login' replace />
  );
}

const Main = () => {
  return (
    <ThemeProvider theme={theme}>
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
              <Route path='/register' element={<Register />} />
              <Route path='*' element={<NotFound />} />
            </Routes>
          </Router>
        </UserProvider>
      </AuthProvider>
    </ThemeProvider>

  )
}

export default Main