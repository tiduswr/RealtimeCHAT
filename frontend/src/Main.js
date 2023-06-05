import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import React, { useContext } from 'react'
import Home from './pages/Home'
import { App } from './pages/App'
import NotFound from './pages/NotFound';
import Login from './pages/Login'
import Register from './pages/Register'
import { createTheme, ThemeProvider } from '@mui/material';
import { AuthContext, AuthProvider } from './providers/AuthProvider'
import '@fontsource/roboto';

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
    <Navigate to='/login' replace/>
  );
}

const Main = () => {
  return (
    <ThemeProvider theme={theme}>
      <AuthProvider>
        <Router>
          <Routes>
            <Route path='/chat'
             element={
              <PrivateRoute>
                <App/>
              </PrivateRoute>}
            />
            <Route path='/' element={<Home/>}/>
            <Route path='/login' element={<Login/>}/>
            <Route path='/register' element={<Register/>}/>
            <Route path='*' element={<NotFound/>} />
          </Routes>
        </Router>
      </AuthProvider>
    </ThemeProvider>
    
  )
}

export default Main