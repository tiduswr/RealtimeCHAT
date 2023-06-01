import React from 'react';
import { List, ListItemButton, ListItemText, Drawer, IconButton, Box, Typography, ListItemAvatar, Avatar, Badge } from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';
import { styled } from '@mui/system';

const ListButtonStyled = styled(ListItemButton)({
  "&.Mui-selected": {
    backgroundColor: "#A5CC82",
    color: "white"
  },
  "&.Mui-selected:hover": {
    backgroundColor: "#A5CC82",
    color: "white"
  },
  "&:hover": {
    backgroundColor: "#00467F",
    color: "white"
  }
});

const MemberList = ({ setTab, tab, privateChats, closeMenu, isMenuOpen, userImage, messageCount }) => {

  return (
    <>
      <Drawer
          variant="persistent"
          anchor="left"
          open={isMenuOpen}
          PaperProps={{
          }}
        >
          <Box sx={{ p: 2 }}>
            <Typography variant="h6" sx={{ mb: 2, fontWeight: 'bold' }}>
              Chats
            </Typography>
            <List sx={{minWidth:'300px'}} >
              <ListButtonStyled
                onClick={() => setTab("CHATROOM")}
                selected={tab === "CHATROOM"}
              >
                <ListItemText primary="Chat Público" />
              </ListButtonStyled>
              {[...privateChats.keys()].map((name, index) => (
                <Badge key={index} badgeContent={messageCount.get(name) || 0} color="secondary" sx={{display: 'flex'}}>
                  <ListButtonStyled
                    key={index}
                    onClick={() => setTab(name)}
                    selected={tab === name}
                  >
                    <ListItemAvatar>
                      <Avatar src={userImage} alt={name} />
                    </ListItemAvatar>
                    <ListItemText primary={name} />
                  </ListButtonStyled>
                </Badge>
              ))}
            </List>
          </Box>
          <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'flex-end', p: 2 }}>
            <IconButton onClick={closeMenu}>
              <CloseIcon/>
            </IconButton>
          </Box>
        </Drawer>
    </>
  );
};

export default MemberList;
