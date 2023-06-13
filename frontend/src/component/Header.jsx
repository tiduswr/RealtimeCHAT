import React, { useContext, useState } from 'react';
import { AppBar, Toolbar, IconButton, Typography, Badge, Link, Box, Menu, MenuItem, Avatar } from '@mui/material';
import MenuIcon from '@mui/icons-material/Menu';
import { styled } from '@mui/system';
import { AuthContext } from '../contexts/AuthProvider.jsx';
import { Link as RouterLink } from 'react-router-dom';
import { UserContext } from '../contexts/UserProvider.jsx'

const BadgeStyled = styled(Badge)({
    display: 'flex',
    '& .MuiBadge-badge': {
        marginTop: '10px',
        marginRight: '20px'
    }
});

const Header = ({ unreadMessagesCount, toggleMenu }) => {
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
                <Toolbar>
                    {toggleMenu ?
                        <>
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
                                <Link component={RouterLink} to="/" color="inherit" underline="none">
                                    WebSocket CHAT
                                </Link>
                            </Typography>
                        </>
                        :
                        <>
                            <Typography variant="h6" component="div" sx={{ flexGrow: 1, fontWeight: 'bold' }}>
                                <Link component={RouterLink} to="/" color="inherit" underline="none">
                                    WebSocket CHAT
                                </Link>
                            </Typography>
                        </>
                    }
                    <Box>
                        <IconButton
                            size="large"
                            edge="start"
                            color="inherit"
                            aria-label="menu"
                            sx={{ mr: 2 }}
                            onClick={handleUserMenuOpen}
                        >
                            <Avatar sx={{ boxShadow: '0px 2px 4px rgba(0, 0, 0, 0.2)', border: '2px solid green' }} alt={userData?.formalName} src={userImage}/>
                        </IconButton>
                        <Menu
                            anchorEl={userMenuAnchorEl}
                            open={isUserMenuOpen}
                            onClose={handleUserMenuClose}
                        >
                            <MenuItem component={RouterLink} to="/perfil" onClick={handleUserMenuClose}>
                                Perfil
                            </MenuItem>
                            <MenuItem onClick={logout}>
                                Logout
                            </MenuItem>
                        </Menu>
                    </Box>
                </Toolbar>
            </AppBar>
        </React.Fragment>
    )
}

export default Header;
