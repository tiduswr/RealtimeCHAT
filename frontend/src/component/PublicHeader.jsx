import React from 'react'
import { AppBar, Toolbar, Typography, Link } from '@mui/material'
import { Link as RouterLink } from 'react-router-dom';

const PublicHeader = () => {

    return (
    <React.Fragment>
        <AppBar position='static' sx={{background: '#00467F'}}>
            <Toolbar>
                <Typography variant="h6" component="div" sx={{ flexGrow: 1, fontWeight: 'bold' }}>
                    <Link component={RouterLink} to="/" color="inherit" underline="none">
                        WebSocket CHAT
                    </Link>
                </Typography>
            </Toolbar>
        </AppBar>
    </React.Fragment>
    )
}

export default PublicHeader;