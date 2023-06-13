import React, { useContext, useEffect, useState } from 'react';
import { over } from 'stompjs';
import SockJS from 'sockjs-client';
import ChatContent from './ChatContent';
import MemberList from '../MemberList';
import { Container } from '@mui/material';
import MessageReceivedAlert from '../message/MessageReceivedAlert';
import { Context } from '../../pages/App/index.js';
import { Api } from '../../api';

var stompClient = null;

const ChatRoom = () => {
  const {
    closeMenu,
    isMenuOpen,
    updateUnreadMessageCount,
    messageCount,
    showAlert,
    publicChats,
    privateChats,
    tab,
    setPrivateChats,
    setTab,
    setShowAlert,
    setMessageCount,
    setPublicChats
  } = useContext(Context);

  const [userData, setUserData] = useState({
    id: 0,
    username: '',
    formalname: '',
    connected: false,
  });

  useEffect(() => {
    const getUserData = async () => {
      try {
        const res = await Api.get('/users/retrieve_profile_info');
        const u = res.data;
        setUserData((prev) => ({ ...prev, id: u.id, username: u.userName, formalname: u.formalName }));
      } catch (error) {
        console.log(error);
      }
    };

    const pullPublicMessage = async () => {
      try {
        const res = await Api.get('/api/v1/messages/retrieve_messages/by/public');
        setPublicChats(res.data);
      } catch (error) {
        console.log(error);
      }
    };

    const onPrivateMessageReceived = (payload) => {
      let payloadData = JSON.parse(payload.body);
      const senderName = payloadData.senderName;

      setPrivateChats((prevPrivateChats) => {
        const updatedPrivateChats = new Map(prevPrivateChats);

        if (updatedPrivateChats.has(senderName)) {
          const messages = [...updatedPrivateChats.get(senderName), payloadData];
          updatedPrivateChats.set(senderName, messages);
        } else {
          updatedPrivateChats.set(senderName, [payloadData]);
        }

        return updatedPrivateChats;
      });

      setTab((prevTab) => {
        if (prevTab !== payloadData.senderName) {
          setShowAlert({ visible: true, sender: payloadData.senderName });
        }
        return prevTab;
      });

      setMessageCount((prevCount) => {
        const updateCount = new Map(prevCount);
        const currentUpdate = updateCount.get(payloadData.senderName) || 0;
        updateCount.set(payloadData.senderName, currentUpdate + 1);

        updateUnreadMessageCount(updateCount);

        return new Map(updateCount);
      });
    };

    const onPublicMessageReceived = (payload) => {
      let payloadData = JSON.parse(payload.body);
      switch (payloadData.status) {
        case 'MESSAGE':
          setPublicChats((prevPublicChats) => prevPublicChats.concat(payloadData));
          break;
        default:
          break;
      }
    };

    const onConnected = () => {
      setUserData((prev) => {
        stompClient.subscribe(`/user/${prev.username}/private`, onPrivateMessageReceived);
        return { ...prev, connected: true };
      });
      stompClient.subscribe('/chatroom/public', onPublicMessageReceived);
    };

    const connectToStompServer = async () => {
      try {
        const token = localStorage.getItem('access_token');
        const ACCESS_TOKEN = JSON.parse(token);

        if (ACCESS_TOKEN) {
          const sock = new SockJS(`http://${window.location.hostname}:8080/ws`);
          stompClient = over(sock, {
            reconnectDelay: 5000,
            heartbeatIncoming: 4000,
            heartbeatOutgoing: 4000,
          });

          stompClient.connect({ 'Authorization': `Bearer ${ACCESS_TOKEN.jwtToken}` }, onConnected, (err) => {
            console.log(err);
          });

        }
      } catch (error) {
        console.log(error);
      }
    };

    getUserData();
    pullPublicMessage();
    connectToStompServer();

    return () => {
      if (stompClient !== null) {
        stompClient.disconnect();
      }
    };
  }, [privateChats, setPrivateChats, setMessageCount, setPublicChats, setTab, setShowAlert, updateUnreadMessageCount]);

  const sendPublicMessage = (message, username) => {
    if (stompClient) {
      const chatMessage = {
        message: message,
        sender: username,
        read: false,
        status: 'MESSAGE',
      };

      stompClient.send('/app/message', {}, JSON.stringify(chatMessage));
    }
  };

  const sendPrivateMessage = (message, username) => {
    if (stompClient) {
      let chatMessage = {
        message: message,
        sender: username,
        read: false,
        receiver: tab,
        status: 'MESSAGE',
      };

      if (userData.username !== tab) {
        privateChats.get(tab).push(chatMessage);
        setPrivateChats(new Map(privateChats));
      }

      stompClient.send('/app/private-message', {}, JSON.stringify(chatMessage));
    }
  };

  return (
    <React.Fragment>
      <Container maxWidth="xl">
        <MemberList
          setTab={setTab}
          tab={tab}
          messageCount={messageCount}
          privateChats={privateChats}
          closeMenu={closeMenu}
          isMenuOpen={isMenuOpen}
        />
        {tab === 'CHATROOM' ? (
          <ChatContent
            chatMessages={publicChats}
            username={userData.username}
            sendMessage={sendPublicMessage}
            placeholder="Insira a mensagem..."
            tab="SALA PÚBLICA"
          />
        ) : (
          <ChatContent
            chatMessages={[...privateChats.get(tab)]}
            username={userData.username}
            sendMessage={sendPrivateMessage}
            placeholder="Insira a mensagem..."
            tab={tab}
          />
        )}
      </Container>
      {showAlert.visible && <MessageReceivedAlert senderName={showAlert.sender} />}
    </React.Fragment>
  );
};

export default ChatRoom;
