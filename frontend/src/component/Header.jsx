import React, { useContext, useState } from 'react';
import { AppBar, Toolbar, IconButton, Typography, Badge, Link, Box, Menu, MenuItem, Avatar } from '@mui/material';
import MenuIcon from '@mui/icons-material/Menu';
import { styled } from '@mui/system';
import { AuthContext } from '../contexts/AuthProvider.jsx';
import { Link as RouterLink } from 'react-router-dom';
import { UserContext } from '../contexts/UserProvider.jsx';
import MessageIcon from '@mui/icons-material/Message';
import LogoutIcon from '@mui/icons-material/Logout';
import AccountBoxIcon from '@mui/icons-material/AccountBox';
import Divider from '@mui/material/Divider';

const BadgeStyled = styled(Badge)({
    display: 'flex',
    '& .MuiBadge-badge': {
        marginTop: '10px',
        marginRight: '20px'
    }
});

const HeaderLink = styled(Typography)({
    display: 'flex',
    alignItems: 'center',
    padding: '10px',
    paddingLeft: '15px',
    paddingRight: '15px',
    '&:hover': {
        backgroundColor: 'rgba(245, 255, 255, 0.18)'
    }
});

const Header = ({ unreadMessagesCount, toggleMenu, includeChatLink }) => {
    const [userMenuAnchorEl, setUserMenuAnchorEl] = useState(null);

    const { userData, userImage } = useContext(UserContext);

    const { logout } = useContext(AuthContext);

    const handleUserMenuOpen = (event) => {
        setUserMenuAnchorEl(event.currentTarget);
    };

    const handleUserMenuClose = () => {
        setUserMenuAnchorEl(null);
    };

    const isUserMenuOpen = Boolean(userMenuAnchorEl);

    return (
        <React.Fragment>
            <AppBar position='static' sx={{ background: '#00467F' }}>
                <Toolbar sx={{ display: 'flex', justifyContent: 'space-between' }}>
                    <Box display="flex" flexDirection="row" sx={{ alignItems: 'center' }}>
                        {toggleMenu ?
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
                            : null
                        }
                        <Typography variant="h6" component="div" sx={{ fontWeight: 'bold', marginRight: '10px' }}>
                            <Link component={RouterLink} to="/" color="inherit" underline="none">
                                WebSocket CHAT
                            </Link>
                        </Typography>
                        {includeChatLink && 
                            <Link component={RouterLink} to="/chat" color="inherit" underline="none">
                                <Box display="flex" flexDirection="row" sx={{ alignItems: 'center' }}>
                                    <HeaderLink>
                                        <MessageIcon sx={{paddingRight: '5px'}}/>Chats
                                    </HeaderLink>
                                </Box>
                            </Link>
                        }
                    </Box>
                    <Box sx={{ display: 'flex', alignItems: 'center' }}>
                        <IconButton
                            size="large"
                            edge="start"
                            color="inherit"
                            aria-label="menu"
                            sx={{ mr: 2 }}
                            onClick={handleUserMenuOpen}
                        >
                            <Avatar sx={{ boxShadow: '0px 2px 4px rgba(0, 0, 0, 0.2)', border: '2px solid green' }} alt={userData?.formalName} src={userImage} />
                        </IconButton>
                        <Menu
                            anchorEl={userMenuAnchorEl}
                            open={isUserMenuOpen}
                            onClose={handleUserMenuClose}
                        >   
                            <Typography sx={{ fontWeight: 'bold', padding: '3px', textAlign: 'center' }}> 
                                {userData?.userName}
                            </Typography>
                            <Divider/>
                            <MenuItem component={RouterLink} to="/perfil" onClick={handleUserMenuClose}>
                                <AccountBoxIcon sx={{paddingRight: '3px'}}/> Perfil
                            </MenuItem>
                            <MenuItem onClick={logout}>
                                <LogoutIcon sx={{paddingRight: '3px'}}/> Logout
                            </MenuItem>
                        </Menu>
                    </Box>
                </Toolbar>
            </AppBar>
        </React.Fragment>
    )
}

export default Header;
