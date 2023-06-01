import React, { useRef, useEffect } from 'react';
import MessageSentProvider from './MessageSentProvider'

import { TextField, Grid, Paper, List, InputAdornment, IconButton, Typography, Box, ListItem } from '@mui/material'
import SendIcon from '@mui/icons-material/Send';
import { useMediaQuery } from '@mui/material';
import MessageIcon from '@mui/icons-material/Message';

const ChatContent = ({ chatMessages, username, message, handleMessage, sendMessage, placeholder, tab, showAlert }) => {

  const messages = useRef(null);

  const scroll = () => {

    const { offsetHeight, scrollHeight, scrollTop } = messages.current

    if(scrollHeight <= scrollTop + offsetHeight + 100){
      messages.current.scrollTo(0, scrollHeight);
    }

  };

  useEffect(() => {
    scroll();
  }, [chatMessages]);

  const isMobile = useMediaQuery('(max-width: 600px)');

  const handleKeyDown = (event) => {
    if (event.key === 'Enter') {
      sendMessage();
    }
  };

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
            <List>
              {chatMessages.map((chat, index) => (
                <ListItem 
                  key={index}
                  sx={{
                      display:'flex', 
                      justifyContent:chat.senderName === username ? 'flex-end' : 'flex-start',
                      padding: 1, 
                      paddingLeft: chat.senderName === username ? '10%' : '0%',
                      paddingRight: chat.senderName === username ? '0%' : '10%',
                      paddingTop: '1px',
                      paddingBottom: '1px'                      
                }}>
                  <MessageSentProvider
                    message={chat.message}
                    senderName={chat.senderName}
                    lastMessageSender={index > 0 ? chatMessages[index-1] : null}
                    showAlert={showAlert}
                  />
                </ListItem>
              ))}
            </List>
          </Paper>
        </Grid>
        <Grid item>
          <TextField
            type="text"
            name="message"
            label={message === '' ? placeholder : ''}
            value={message}
            onChange={handleMessage}
            onKeyDown={handleKeyDown}
            variant="outlined"
            fullWidth
            sx={{
              '.MuiOutlinedInput-root': {
                borderRadius: '50px',
              }
            }}
            InputLabelProps={{
              shrink: false
            }}
            InputProps={{
              endAdornment: (
                <InputAdornment position="end">
                  <IconButton onClick={sendMessage} edge="end">
                    <SendIcon />
                  </IconButton>
                </InputAdornment>
              ),
            }}
          />
        </Grid>
      </Grid>
    </Box>
  )
}

export default ChatContent