import { useCallback, useContext } from 'react';

import { Api } from '../api';
import { buildContact } from '../calls/chatInfoCalls';
import { UserContext } from '../contexts/UserProvider';
import { Context } from '../pages/App';
import { NotificationContext } from '../contexts/NotificationProvider';
import { tryGetErrorMessage } from '../errorParser';
import { MESSAGE_SERVICE_URI, 
          websocketPrivateSubscribtionResolver, 
          websocketPublicSubscribtionResolver,
          websocketErrorSubscribtionResolver } from '../hostResolver';

export const useWebsocketMessagesConfig = ({ setTab, contacts, setContacts, setChatMessages, setStompClient }) => {
  const { setMessageCount, setUnreadMessageCount } = useContext(Context);
  const { setAlert } = useContext(NotificationContext);
  const { setUserData, userData } = useContext(UserContext);

  const sendPrivateMessagesRead = useCallback((username, receiver) => {
    setStompClient(stompClient => {
      if (stompClient) {
        let chatMessage = {
          message: 'READ',
          sender: username,
          read: false,
          receiver: receiver,
          status: 'READ',
        };
        stompClient.send(
          '/app/private-message', {}, JSON.stringify(chatMessage));
      }
      return stompClient;
    })
  }, [setStompClient])

  const onPrivateMessageReceived = useCallback((payload) => {
    let payloadData = JSON.parse(payload.body);
    const senderName = payloadData.sender;

    setTab(tab => {
      const userTabIsNotOpen = (senderName !== userData.userName) && (!tab || (tab?.userName !== senderName));
      
      switch (payloadData.status) {
        case 'MESSAGE':
          if (userTabIsNotOpen) {
            setMessageCount(prev => {
              const newMap = new Map(prev);
              let prevCount = newMap.get(senderName);

              let count = prevCount ? prevCount : 0;
              count++;
              newMap.set(senderName, count);

              return newMap;
            })

            setContacts(contacts => {
              const isNewContact = !contacts.some(ctt => ctt.userName === senderName);
              if (isNewContact) {
                buildContact(senderName).then((data) => {
                  setContacts([...contacts, data]);
                });
              }
              return contacts;
            });

            setUnreadMessageCount(prev => ++prev);
            setAlert({ message: `${senderName} diz: ${payloadData.message}`, type: 'info' })

          } else if(tab){
            setChatMessages(prev => [...prev, payloadData]);

            let receiver = tab.userName;
            Api.put(`${MESSAGE_SERVICE_URI}/messages/mark_messages_as_read/${receiver}`)
              .then(res => {
                if (res.status === 204) {
                  sendPrivateMessagesRead(userData.userName, receiver)
                }
              })
              .catch(error => {
                setAlert({ 
                  message: tryGetErrorMessage(error), 
                  type: 'error'
                });
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
      return tab;
    })
  }, [sendPrivateMessagesRead, setChatMessages, setContacts,
    setMessageCount, setAlert, setUnreadMessageCount, setTab,
    userData]);

  const onPublicMessageReceived = useCallback((payload) => {
    let payloadData = JSON.parse(payload.body);

    switch (payloadData.status) {
      case 'MESSAGE':
        setTab(prev => {
          const isUserOnPublicChat = (prev == null);

          if(isUserOnPublicChat){
            setChatMessages(prev => [...prev, payloadData]);
          }
          return prev;
        })
        break;
      default:
        break;
    }
  }, [setChatMessages, setTab]);

  const onError = useCallback((error) => {
    let errorMessage = error?.body ? error.body : error;

    if(errorMessage?.message){
      errorMessage = errorMessage.message;
    }else{
      try{
        const payload = JSON.parse(errorMessage);
        errorMessage = payload.message;
      }catch(ex){
        errorMessage = tryGetErrorMessage(error);
      }
    }

    setAlert({ 
      message: errorMessage, 
      type: 'error'
    });
  }, [setAlert]);

  const onConnected = useCallback((client) => {
    setUserData(userData => {
      client.subscribe(websocketPrivateSubscribtionResolver(userData.userName), onPrivateMessageReceived);
      client.subscribe(websocketPublicSubscribtionResolver(), onPublicMessageReceived);
      client.subscribe(websocketErrorSubscribtionResolver(userData.userName), onError);
      return userData;
    })
  }, [onPrivateMessageReceived, onPublicMessageReceived, setUserData, onError])

  return {
    sendPrivateMessagesRead, onConnected
  }
}