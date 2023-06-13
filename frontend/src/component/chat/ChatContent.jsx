import React, { useRef, useEffect } from 'react';

import { Grid, Paper, Typography, Box } from '@mui/material'
import { useMediaQuery } from '@mui/material';
import MessageIcon from '@mui/icons-material/Message';
import MessageBox from '../message/MessageBox';
import ChatMessagesList from './ChatMessagesList';

const ChatContent = ({ chatMessages, username, sendMessage, placeholder, tab, showAlert }) => {

  const messages = useRef('null');

  const scroll = () => {

    const { offsetHeight, scrollHeight, scrollTop } = messages.current

    if(scrollHeight <= scrollTop + offsetHeight + 120){
      messages.current.scrollTo(0, scrollHeight);
    }

  };

  useEffect(() => {
    scroll();
  }, [chatMessages, messages]);

  const isMobile = useMediaQuery('(max-width: 600px)');

  return (
    <Box sx={{ marginTop: '20px'}}>
      <Grid container direction="column" spacing={1}>
        <Grid item>
          <Paper elevation={2} sx={{padding:'10px', background: '#00467F', display: 'flex', alignItems: 'center'}}>
            <MessageIcon sx={{color: 'white', paddingRight: '5px'}}/>
            <Typography 
              variant='caption' 
              sx={{
                fontSize:'20px', 
                fontWeight:'bold', 
                color: 'white'
              }}>{tab.toUpperCase()}</Typography>
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
            <ChatMessagesList 
              chatMessages={chatMessages} 
              username={username} 
              showAlert={showAlert}
            />
          </Paper>
        </Grid>
        <Grid item>
          <MessageBox
            sendMessage={sendMessage}
            placeholder={placeholder}
            userName={username}
          />
        </Grid>
      </Grid>
    </Box>
  )
}

export default ChatContent