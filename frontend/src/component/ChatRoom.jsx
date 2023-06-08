import React, { useContext, useState } from 'react';
import { over } from 'stompjs';
import SockJS from 'sockjs-client';
import ChatContent from './ChatContent';
import MemberList from './MemberList';
import { Container } from '@mui/material';
import MessageReceivedAlert from './MessageReceivedAlert';
import { Context } from '../pages/App/index.js'
import { Api } from '../api'

var stompClient = null;

const ChatRoom = () => {

  const { closeMenu, isMenuOpen, updateUnreadMessageCount,
    messageCount, showAlert, publicChats, privateChats,
    tab,  setPrivateChats, setTab,
    setShowAlert, setMessageCount,
    setPublicChats 
  } = useContext(Context); 

  const [userData, setUserData] = useState({
    username: '',
    connected: false,
  });

  useState(() => {

    const token = localStorage.getItem('access_token');
    const ACCESS_TOKEN = JSON.parse(token);

    setUserData(prevData => {
      return {...prevData, username : ACCESS_TOKEN.userName}
    })

    const pullPublicMessage = () => {
      Api.get('/api/v1/messages/retrieve_messages/by/public')
        .then(res => {
          setPublicChats(res.data);
        })
    }

    const onPrivateMessageReceived = payload => {
      
      let payloadData = JSON.parse(payload.body);
      const senderName = payloadData.senderName;
  
      setPrivateChats(prevPrivateChats => {
        const updatedPrivateChats = new Map(prevPrivateChats);
  
        if (updatedPrivateChats.has(senderName)) {
          const messages = [...updatedPrivateChats.get(senderName), payloadData];
          updatedPrivateChats.set(senderName, messages);
        } else {
          updatedPrivateChats.set(senderName, [payloadData]);
        }
  
        return updatedPrivateChats;
      });
  
      //Pega o valor atualizado de tab
      setTab(prevTab => {
        if (prevTab !== payloadData.senderName) {
          setShowAlert({ visible: true, sender: payloadData.senderName });
        }
        return prevTab;
      });
  
      //Incrementa mensagens recebidas não lidas
      setMessageCount(prevCount => {
        const updateCount = new Map(prevCount);
        const currentUpdate = updateCount.get(payloadData.senderName) || 0;
        updateCount.set(payloadData.senderName, currentUpdate + 1);
  
        updateUnreadMessageCount(updateCount);
  
        return new Map(updateCount);
      })
    }
  
    const onPublicMessageReceived = payload => {
      let payloadData = JSON.parse(payload.body);
      switch(payloadData.status){
        case 'JOIN':
          if(!privateChats.get(payloadData.senderName) && payloadData.senderName !== userData.username){
            privateChats.set(payloadData.senderName, []);
            setPrivateChats(new Map(privateChats));
          }
          break;
        case 'MESSAGE':
          setPublicChats(prevPublicChats => prevPublicChats.concat(payloadData));
          break;
        default:
          break;
      }
    }

    const onConnected = () =>{
      setUserData({...userData, connected : true});
      stompClient.subscribe('/chatroom/public', onPublicMessageReceived);
      stompClient.subscribe(`/user/${userData.username}/private`, onPrivateMessageReceived)
    }

    const sock = new SockJS(`http://${window.location.hostname}:8080/ws`);
    stompClient = over(sock, {
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
    });

    stompClient.connect({ 'Authorization': `Bearer ${ACCESS_TOKEN.jwtToken}` }, onConnected, (err) => {
      console.log(err);
    });

    pullPublicMessage();

    return () => {
      if (stompClient) {
        stompClient.deactivate();
      }
    };

  }, [])

  const handleMessage = e => {
    const {value} = e.target;
    setUserData({...userData, message : value})
  } 

  const sendPublicMessage = () => {
    if(stompClient){
      let chatMessage = {
        message: userData.message,
        sender: userData.username,
        read: false,
        status: 'MESSAGE'
      };

      stompClient.send('/app/message', {}, JSON.stringify(chatMessage));
      setUserData({...userData, 'message' : ''});
    }
  }

  const sendPrivateMessage = () => {
    if(stompClient){
      let chatMessage = {
        message: userData.message,
        sender: userData.username,
        read: false,
        receiver:tab,
        status: 'MESSAGE'
      };

      if(userData.username !== tab){
        privateChats.get(tab).push(chatMessage);
        setPrivateChats(new Map(privateChats));
      }

      stompClient.send('/app/private-message', {}, JSON.stringify(chatMessage));
      setUserData({...userData, 'message' : ""});
    }
  }

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
          {tab === "CHATROOM" ?
            <ChatContent
              chatMessages={publicChats}
              username={userData.username}
              message={userData.message}
              handleMessage={handleMessage}
              sendMessage={sendPublicMessage}
              placeholder={`Insira a mensagem...`}
              tab={'SALA PÚBLICA'}
            />
          :
            <ChatContent 
              chatMessages={[...privateChats.get(tab)]}
              username={userData.username}
              message={userData.message}
              handleMessage={handleMessage}
              sendMessage={sendPrivateMessage}
              placeholder={`Insira a mensagem...`}
              tab={tab}
            />
          }
        </Container>      
      {showAlert.visible && 
        <MessageReceivedAlert senderName={showAlert.sender} />
      }
    </React.Fragment>
  )
}

export default ChatRoom