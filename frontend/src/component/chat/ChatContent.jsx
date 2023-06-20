import React, { useRef, useEffect, useState, useContext, useCallback } from 'react';

import { Grid, Paper, Typography, Box } from '@mui/material'
import { useMediaQuery } from '@mui/material';
import MessageIcon from '@mui/icons-material/Message';
import MessageBox from '../message/MessageBox';
import ChatMessagesList from './ChatMessagesList';
import { UserContext } from '../../contexts/UserProvider';
import { Api } from '../../api';
import LoadingSpinner from '../../component/LoadingSpinner'

const ChatContent = ({ stompClient, tab, chatMessages, setChatMessages, setMessageCount }) => {
 
  const [loadingMessage, setLoadingMessage] = useState(true);
  const { userData } = useContext(UserContext);

  const messages = useRef('null');

  useEffect(() => {
    const pullPublicMessage = async () => {
      try {
        const res = await Api.get('/api/v1/messages/retrieve_messages/by/public');
        setChatMessages(res.data);
      } catch (error) {
        console.log(error);
      }
      setLoadingMessage(false);
    };

    const pullPrivateMessage = async () => {
      try {
        const res = await Api.get(`/api/v1/messages/retrieve_messages/by/${tab.userName}`);
        setChatMessages(res.data);
      } catch (error) {
        console.log(error);
      }
      setLoadingMessage(false);
    };

    setLoadingMessage(true);
    switch (tab) {
      case null:
        pullPublicMessage();
        break;
      default:
        pullPrivateMessage();
        break;
    }
  }, [tab, setChatMessages, setMessageCount])

  const sendPublicMessage = useCallback((message, username) => {
    if (stompClient) {
      const chatMessage = {
        message: message,
        sender: username,
        read: false,
        status: 'MESSAGE',
      };

      stompClient.send('/app/message', {}, JSON.stringify(chatMessage));
    }
  }, [stompClient]);

  const sendPrivateMessage = useCallback((message, username) => {
    if (stompClient) {
      let chatMessage = {
        message: message,
        sender: username,
        read: false,
        receiver: tab.userName,
        status: 'MESSAGE',
      };

      stompClient.send('/app/private-message', {}, JSON.stringify(chatMessage));
    }
  }, [stompClient, tab]);

  const scroll = () => {

    const { offsetHeight, scrollHeight, scrollTop } = messages.current

    if (scrollHeight <= scrollTop + offsetHeight + 120) {
      messages.current.scrollTo(0, scrollHeight);
    }

  };

  useEffect(() => {
    scroll();
  }, [chatMessages, messages]);

  const isMobile = useMediaQuery('(max-width: 600px)');

  return (
    <Box sx={{ marginTop: '20px' }}>
      <Grid container direction="column" spacing={1}>
        <Grid item>
          <Paper elevation={2} sx={{ padding: '10px', background: '#00467F', display: 'flex', alignItems: 'center' }}>
            <MessageIcon sx={{ color: 'white', paddingRight: '5px' }} />
            <Typography
              variant='caption'
              sx={{
                fontSize: '20px',
                fontWeight: 'bold',
                color: 'white'
              }}>{tab ? tab.formalName : 'Chat Público'}</Typography>
          </Paper>
        </Grid>
        <Grid item>
          <Paper
            ref={messages}
            elevation={3}
            style={{
              height: isMobile ? '55vh' : '70vh',
              overflowY: 'auto',
              padding: '10px',
              background: 'linear-gradient(to right, #00467F, #A5CC82)'
            }}>
            {loadingMessage ?
              <LoadingSpinner iconSx={{ backgroundColor: 'white', borderRadius: '50%', padding: 2 }} />
              :
              <ChatMessagesList
                chatMessages={chatMessages}
                username={userData?.userName}
              />
            }
          </Paper>
        </Grid>
        <Grid item>
          <MessageBox
            sendMessage={tab === null ? sendPublicMessage : sendPrivateMessage}
            placeholder={"Insira a mensagem..."}
            userName={userData?.userName}
          />
        </Grid>
      </Grid>
    </Box>
  )
}

export default ChatContent