import React from 'react'
import { AppBar, Toolbar, IconButton, Typography, Button, Badge } from '@mui/material'
import MenuIcon from '@mui/icons-material/Menu';
import { styled } from '@mui/system'

const BadgeStyled = styled(Badge)({
    display: 'flex', 
    '& .MuiBadge-badge': {
         marginTop: '10px', 
         marginRight: '20px' 
    }
})

const Header = ({ toggleMenu, unreadMessagesCount }) => {
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
                <Button color="inherit">Login</Button>
            </Toolbar>
        </AppBar>
    </React.Fragment>
  )
}

export default Header