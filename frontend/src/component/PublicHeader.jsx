import React, { useContext } from 'react'
import { AppBar, Toolbar, Typography, Link, Button } from '@mui/material'
import { Link as RouterLink, useNavigate } from 'react-router-dom';
import LoginIcon from '@mui/icons-material/Login';
import { AuthContext } from '../contexts/AuthProvider'

const PublicHeader = ({ hideLoginButton }) => {
    const navigate = useNavigate();
    const { isAuthenticated } = useContext(AuthContext)

    return (
    <AppBar position='static' sx={{background: '#00467F'}}>
        <Toolbar>
            <Typography variant="h6" component="div" sx={{ flexGrow: 1, fontWeight: 'bold' }}>
                <Link component={RouterLink} to="/" color="inherit" underline="none">
                    WebSocket CHAT
                </Link>
            </Typography>
            {(!hideLoginButton && !isAuthenticated) &&
                <Button 
                    variant="outlined" 
                    color="inherit" 
                    startIcon={<LoginIcon sx={{color: 'white'}}/>}
                    onClick={() => navigate('/login')}                                    
                >
                    Login
                </Button>
            }
        </Toolbar>
    </AppBar>
    )
}

export default PublicHeader;