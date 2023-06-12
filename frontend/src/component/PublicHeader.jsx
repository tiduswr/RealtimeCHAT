import React from 'react'
import { AppBar, Toolbar, Typography, Link, Button } from '@mui/material'
import { Link as RouterLink, useNavigate } from 'react-router-dom';
import LoginIcon from '@mui/icons-material/Login';

const PublicHeader = ({ hideLoginButton }) => {
    const navigate = useNavigate();

    return (
    <AppBar position='static' sx={{background: '#00467F'}}>
        <Toolbar>
            <Typography variant="h6" component="div" sx={{ flexGrow: 1, fontWeight: 'bold' }}>
                <Link component={RouterLink} to="/" color="inherit" underline="none">
                    WebSocket CHAT
                </Link>
            </Typography>
            {!hideLoginButton &&
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