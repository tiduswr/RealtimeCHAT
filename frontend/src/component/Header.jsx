import React, { useContext } from 'react'
import { Context } from '../pages/App/index.js'
import { AppBar, Toolbar, IconButton, Typography, Badge, Button } from '@mui/material'
import MenuIcon from '@mui/icons-material/Menu';
import { styled } from '@mui/system'
import { AuthContext } from '../providers/AuthProvider.jsx';

const BadgeStyled = styled(Badge)({
    display: 'flex', 
    '& .MuiBadge-badge': {
         marginTop: '10px', 
         marginRight: '20px' 
    }
})

const Header = () => {
    
    const { logout } = useContext(AuthContext);
    const { unreadMessagesCount, toggleMenu } = useContext(Context);

    return (
    <React.Fragment>
        <AppBar position='static' sx={{background: '#00467F'}}>
            <Toolbar>
                <BadgeStyled badgeContent={unreadMessagesCount} color="secondary">
                    <IconButton
                        size="large"
                        edge="start"
                        color="inherit"
                        aria-label="menu"
                        sx={{ mr: 2 }}
                        onClick={toggleMenu}
                    >
                        <MenuIcon />
                    </IconButton>
                </BadgeStyled>
                <Typography variant="h6" component="div" sx={{ flexGrow: 1, fontWeight: 'bold' }}>
                    WebSocket CHAT
                </Typography>
                <Button onClick={logout}>Logout</Button>
            </Toolbar>
        </AppBar>
    </React.Fragment>
    )
}

export default Header