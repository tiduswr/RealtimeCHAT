import React from 'react';
import {over} from 'stompjs';
import SockJS from 'sockjs-client';
import ChatRegister from './ChatRegister';
import ChatContent from './ChatContent';
import MemberList from './MemberList';
import { Container } from '@mui/material';
import MessageReceivedAlert from './MessageReceivedAlert';

var stompClient = null;

const ChatRoom = ({ 
  closeMenu, 
  isMenuOpen, 
  updateUnreadMessageCount,
  messageCount,
  showAlert,
  publicChats,
  privateChats,
  tab,
  userData,
  setPrivateChats,
  setTab,
  setShowAlert,
  setMessageCount,
  setUserData,
  setPublicChats

}) => {

  const handleUsername=(event)=>{
    const {value}=event.target;
    setUserData({...userData,"username": value});
  }

  const handleMessage = e => {
    const {value} = e.target;
    setUserData({...userData, message : value})
  }

  const registerUser = () => {
    let Sock = new SockJS(`http://${window.location.hostname}:8080/ws`);
    stompClient = over(Sock);
    stompClient.debug = null;
    stompClient.connect({}, onConnected, onError);
  }

  const onConnected = () =>{
    setUserData({...userData, connected : true});
    stompClient.subscribe('/chatroom/public', onPublicMessageReceived);
    stompClient.subscribe(`/user/${userData.username}/private`, onPrivateMessageReceived)
    userJoin();
  }

  const userJoin = () => {
    let chatMessage = {
      senderName: userData.username,
      status: 'JOIN'
    };
    stompClient.send('/app/message',{},JSON.stringify(chatMessage));
  }

  const onError = err =>{
    console.log("Erro: " + err);
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
        publicChats.push(payloadData);
        setPublicChats([...publicChats])
        break;
      default:
        break;
    }
  }

  const sendPublicMessage = () => {
    if(stompClient){
      let chatMessage = {
        senderName: userData.username,
        message: userData.message,
        read: false,
        status: 'MESSAGE'
      };
      stompClient.send('/app/message',{},JSON.stringify(chatMessage));
      setUserData({...userData, 'message' : ""});
    }
  }

  const sendPrivateMessage = () => {
    if(stompClient){
      let chatMessage = {
        senderName: userData.username,
        receiverName:tab,
        message: userData.message,
        read: false,
        status: 'MESSAGE'
      };

      if(userData.username !== tab){
        privateChats.get(tab).push(chatMessage);
        setPrivateChats(new Map(privateChats));
      }

      stompClient.send('/app/private-message',{},JSON.stringify(chatMessage));
      setUserData({...userData, 'message' : ""});
    }
  }

  return (
    <React.Fragment>
      {userData.connected ? 
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
      :

        <ChatRegister 
          username={userData.username} 
          handleUsername={handleUsername}
          registerUser={registerUser}
        />

      }      
      {showAlert.visible && 
        <MessageReceivedAlert senderName={showAlert.sender} />
      }
    </React.Fragment>
  )
}

export default ChatRoom