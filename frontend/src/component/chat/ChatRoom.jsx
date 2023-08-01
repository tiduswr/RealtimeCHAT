import React, { useCallback, useContext, useEffect, useState } from 'react';
import { over } from 'stompjs';
import SockJS from 'sockjs-client';
import { Container } from '@mui/material';
import MessageReceivedAlert from '../message/MessageReceivedAlert';
import { Context } from '../../pages/App/index.js';
import { UserContext } from '../../contexts/UserProvider';
import ChatPerspective from './ChatPerspective';
import { Api } from '../../api';

var stompClient = null;

const ChatRoom = () => {
  const {
    closeMenu,
    isMenuOpen,
    showAlert,
    setShowAlert,
    setMessageCount,
    setUnreadMessageCount
  } = useContext(Context);
  const { userData } = useContext(UserContext);

  const [chatMessages, setChatMessages] = useState([]);
  const [tab, setTab] = useState(null);
  const [connecting, setConnecting] = useState(true);
  
  const sendPrivateMessagesRead = useCallback((username, receiver) => {
    if (stompClient) {
      let chatMessage = {
        message: 'READ',
        sender: username,
        read: false,
        receiver: receiver,
        status: 'READ',
      };
      stompClient.send('/app/private-message', {}, JSON.stringify(chatMessage));
    }
  }, []);

  useEffect(() => {

    const userTabIsNotOpen = (senderName) => {
      return senderName !== userData.userName && tab?.userName !== senderName;
    };

    const onPrivateMessageReceived = (payload) => {
      let payloadData = JSON.parse(payload.body);
      const senderName = payloadData.sender;

      switch (payloadData.status) {
        case 'MESSAGE':
          if (userTabIsNotOpen(senderName)) {
            setMessageCount(prev => {
              const newMap = new Map(prev);
              let count = newMap.get(senderName);
              count++;
              newMap.set(senderName, count);
              return newMap;
            })
            setUnreadMessageCount(prev => ++prev);
            setShowAlert({ visible: true, sender: senderName })
          } else {
            setChatMessages(prev => [...prev, payloadData]);
            let receiver = tab.userName;
            Api.put(`/api/v1/messages/mark_messages_as_read/${receiver}`)
              .then(res => {
                if (res.status === 204) {
                  sendPrivateMessagesRead(userData.userName, receiver)
                }
              }).catch(error => {
                console.log(error);
              })
          }
          break;
        case 'READ':
          setChatMessages(prev => {
            return prev.map(e => {
              return e.sender === userData.userName ? { ...e, read: true } : e;
            });
          })
          break;
        default:
          break;
      }
    };

    const onPublicMessageReceived = (payload) => {
      let payloadData = JSON.parse(payload.body);

      switch (payloadData.status) {
        case 'MESSAGE':
          setChatMessages(prev => [...prev, payloadData]);
          break;
        default:
          break;
      }
    };

    const onConnected = () => {
      stompClient.subscribe(`/user/${userData.userName}/private`, onPrivateMessageReceived);
      stompClient.subscribe('/chatroom/public', onPublicMessageReceived);
    };

    const connectToStompServer = async () => {
      try {
        const token = localStorage.getItem('access_token');
        const ACCESS_TOKEN = JSON.parse(token);

        if (ACCESS_TOKEN) {
          const port = window.location.port || (window.location.protocol === 'https:' ? '443' : '80');
          const sock = new SockJS(`${window.location.protocol}//${window.location.hostname}:${port}/backend/ws/`);
          stompClient = over(sock, {
            reconnectDelay: 5000,
            heartbeatIncoming: 4000,
            heartbeatOutgoing: 4000,
          });

          stompClient.debug = null;

          stompClient.connect({ 'Authorization': `Bearer ${ACCESS_TOKEN.jwtToken}` }, onConnected, (err) => {
            console.log(err);
          });

        }
      } catch (error) {
        console.log(error);
      }
      setConnecting(false);
    };

    if (userData && !stompClient) connectToStompServer();

    return () => {
      if (stompClient) {
        if(stompClient.connected) stompClient.disconnect();
        stompClient = null;
      }
    };

  }, [userData, setShowAlert, sendPrivateMessagesRead, setMessageCount, setUnreadMessageCount, tab]);

  if(connecting) return null;

  return (
    <React.Fragment>
      <Container maxWidth="xl">
        <ChatPerspective
          closeMenu={closeMenu}
          isMenuOpen={isMenuOpen}
          stompClient={stompClient}
          chatMessages={chatMessages}
          setChatMessages={setChatMessages}
          tab={tab}
          setTab={setTab}
          sendPrivateMessagesRead={sendPrivateMessagesRead}
        />
      </Container>
      {showAlert.visible && <MessageReceivedAlert senderName={showAlert.sender} />}
    </React.Fragment>
  );
};

export default ChatRoom;
