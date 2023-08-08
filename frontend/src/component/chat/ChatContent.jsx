import React, { useRef, useEffect, useLayoutEffect, useState, useContext, useCallback } from 'react';

import { Grid, Paper, Typography, Box } from '@mui/material'
import { useMediaQuery } from '@mui/material';
import MessageIcon from '@mui/icons-material/Message';
import MessageBox from '../message/MessageBox';
import ChatMessagesList from './ChatMessagesList';
import { UserContext } from '../../contexts/UserProvider';
import { Api } from '../../api';
import LoadingSpinner from '../../component/LoadingSpinner'
import { NotificationContext } from '../../contexts/NotificationProvider';
import { tryGetErrorMessage } from '../../errorParser';

const PAGE_SIZE = 15;

const ChatContent = ({ stompClient, tab, chatMessages, setChatMessages, setMessageCount }) => {

  const isMobile = useMediaQuery('(max-width: 600px)');
  const [loadingMessage, setLoadingMessage] = useState(true);
  const { userData } = useContext(UserContext);
  const { setAlert } = useContext(NotificationContext);
  const [page, setPage] = useState(0);
  const [lengthPages, setLengthPages] = useState(0);
  const [preventFetchMessages, setPreventFetchMessages] = useState(false);
  const messages = useRef(null);

  useEffect(() => {
    if(!preventFetchMessages){
      const pullPublicMessage = async () => {
        try {
          const res = await Api.get(`/api/v1/messages/retrieve_messages/by/public/in/page/${page}/size/${PAGE_SIZE}`);
          const { totalPages, content } = res.data;

          setLengthPages(totalPages);

          if(page === 0){
            setChatMessages(content);
          }else{
            setChatMessages(prev => {
              return content.concat(prev);
            });
          }
        } catch (error) {
          setAlert({ 
            message: tryGetErrorMessage(error), 
            type: 'error'
          });
        }
        setLoadingMessage(false);
      };

      const pullPrivateMessage = async () => {
        try {
          const res = await Api.get(`/api/v1/messages/retrieve_messages/by/${tab.userName}/in/page/${page}/size/${PAGE_SIZE}`);
          const { totalPages, content } = res.data;

          setLengthPages(totalPages);
          
          if(page === 0){
            setChatMessages(content);
          }else{
            setChatMessages(prev => {
              return content.concat(prev);
            });
          }
        } catch (error) {
          setAlert({ 
            message: tryGetErrorMessage(error), 
            type: 'error'
          });
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
    }
  }, [tab, setChatMessages, setMessageCount, setAlert, page, preventFetchMessages])

  

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
    const COMPENSATION_HEIGHT = 150;
    const { offsetHeight, scrollHeight, scrollTop } = messages.current

    if (scrollHeight <= scrollTop + offsetHeight + COMPENSATION_HEIGHT) {
      messages.current.scrollTo(0, scrollHeight);
    }
  };

  const isEndOfList = () =>{
    const list = messages.current;
    const isBottomOnMobile = isMobile && ((list.scrollHeight - Math.round(list.scrollTop)) === list.clientHeight);
    const isBottomOnPc = (list.scrollTop + list.clientHeight) >= list.scrollHeight;

    if (isBottomOnMobile || isBottomOnPc) {
      setChatMessages(prev => {
        if(prev.length > PAGE_SIZE){
          setPreventFetchMessages(true);
          setPage(0);
          return prev.slice(PAGE_SIZE*(-1));
        }

        return prev;
      })
    }
  }

  useLayoutEffect(() => {
    scroll();
  }, [chatMessages]);

  

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
            onScroll={isEndOfList}
            elevation={3}
            style={{
              height: isMobile ? '55vh' : '70vh',
              overflowY: 'auto',
              paddingLeft: '10px',
              paddingRight: '10px',
              paddingBottom: '10px',
              background: 'linear-gradient(to right, #00467F, #A5CC82)'
            }}>
            {loadingMessage ?
              <LoadingSpinner iconSx={{ backgroundColor: 'white', borderRadius: '50%', padding: 2 }} />
              :
              <ChatMessagesList
                setPreventFetchMessages={setPreventFetchMessages}
                page={page}
                setPage={setPage}
                lengthPages={lengthPages}
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