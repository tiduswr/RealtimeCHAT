import React, { useContext, useEffect, useState } from 'react';
import { List, ListItemButton, ListItemText, Drawer, IconButton, Box, Typography, ListItemAvatar, Avatar, Badge } from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';
import { styled } from '@mui/system';
import SearchIcon from '@mui/icons-material/Search';
import BasicModal from './SearchModal';
import { Api } from '../api';
import GroupIcon from '@mui/icons-material/Group';
import { UserContext } from '../contexts/UserProvider';

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

const MemberList = ({ setTab, tab, closeMenu, isMenuOpen, messageCount, setMessageCount, setUnreadMessageCount, sendPrivateMessagesRead }) => {

  const [openSearchModal, setOpenSearchModal] = useState(false);
  const [contacts, setContacts] = useState([])
  const { userData } = useContext(UserContext);

  useEffect(() => {

    const getUsers = async () => {
      try {
        const usersResponse = await Api.get('/api/v1/messages/retrieve_users/talked');
        const usersData = usersResponse.data;
    
        const modifiedUsersData = await Promise.all(usersData.map(async e => {
          const imageResponse = await Api.get(`/users/retrieve_profile_image/${e.userName}`, { responseType: 'arraybuffer' });
          const image = URL.createObjectURL(new Blob([imageResponse.data], { type: 'image/png' }));
          return { ...e, image };
        }));

        const unreadedMessages = await Promise.all(modifiedUsersData.map(async e => {
          const res = await Api.get(`/api/v1/messages/retrieve_count/by/${e.userName}`);
          const data = res.data.count;
          return {userName: e.userName, count: data};
        }));

        setMessageCount(prev => {
          const newMap = new Map(prev);
          unreadedMessages.forEach(e => {
            newMap.set(e.userName, e.count)
          })
          return newMap;
        })
        setContacts(modifiedUsersData);
      } catch (error) {
        console.log(error);
      }
    };

    getUsers();

  },[setMessageCount])
    
  useEffect(() => {
    const retrieveMessagesCount = async () =>{
      try{
        const res = await Api.get('/api/v1/messages/retrieve_count/total')
        const data = res.data.count;
        setUnreadMessageCount(parseInt(data));
      }catch(error){
        console.log(error);
      }
    }
    retrieveMessagesCount();
  }, [messageCount, setUnreadMessageCount])

  const handleTabChange = (userName, formalName) => {
    Api.put(`/api/v1/messages/mark_messages_as_read/${userName}`)
      .then(res => {
        if(res.status === 204){
          sendPrivateMessagesRead(userData.userName, userName)
          setMessageCount(prev => {
            const newMap = new Map(prev);
            newMap.set(userName, 0);
            return newMap;
          })
          setTab({ userName, formalName });
        }
      }).catch(error => {
        console.log(error);
      })
  }

  return (
    <>
      <Drawer
          variant="persistent"
          anchor="left"
          open={isMenuOpen}
          PaperProps={{
          }}
        >
          <Box sx={{ p: 2, mb: 2 }}>
            <Box sx={{ display: 'flex', textAlign: 'center', alignItems: 'center', justifyContent: 'space-between' }}>
              <Typography variant="h6" sx={{ fontWeight: 'bold' }}>
                Chats
              </Typography>
              <IconButton onClick={(e) => setOpenSearchModal(!openSearchModal)}>
                <SearchIcon/>
              </IconButton>
            </Box>
            <List sx={{minWidth:'300px'}} >
              <ListButtonStyled
                onClick={() => setTab(null)}
                selected={tab === null}
              >
                <GroupIcon sx={{marginRight: 2, marginLeft: 1}}/>
                <ListItemText primary="Chat Público" />
              </ListButtonStyled>
              {contacts.map((u, index) => (
                <Badge key={index} badgeContent={messageCount.get(u.userName) || 0} color="secondary" sx={{display: 'flex'}}>
                  <ListButtonStyled
                    key={index}
                    onClick={() => handleTabChange(u.userName, u.formalName)}
                    selected={tab && tab.userName === u.userName}
                  >
                    <ListItemAvatar>
                      <Avatar src={u.image} alt={u.formalName} />
                    </ListItemAvatar>
                    <ListItemText primary={u.formalName} />
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
        {openSearchModal && <BasicModal closeFunc={setOpenSearchModal} setContacts={setContacts} />}
    </>
  );
};

export default MemberList;
