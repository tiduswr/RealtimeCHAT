import { useCallback, useContext } from 'react';

import { Api } from '../api';
import { buildContact } from '../calls/chatInfoCalls';
import { UserContext } from '../contexts/UserProvider';
import { Context } from '../pages/App';
import { NotificationContext } from '../contexts/NotificationProvider';
import { tryGetErrorMessage } from '../errorParser';

export const useWebsocketMessagesConfig = ({ setTab, setContacts, setChatMessages, setStompClient }) => {
  const { setMessageCount, setUnreadMessageCount } = useContext(Context);
  const { setAlert } = useContext(NotificationContext);
  const { userData } = useContext(UserContext);

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

  const userTabIsNotOpen = useCallback((senderName) => {
    let userTabIsNotOpen;
    setTab(tab => {
      userTabIsNotOpen = senderName !== userData.userName && tab?.userName !== senderName;;
      return tab;
    })
    return userTabIsNotOpen;
  }, [setTab, userData]);

  const onPrivateMessageReceived = useCallback((payload) => {
    let payloadData = JSON.parse(payload.body);
    const senderName = payloadData.sender;

    switch (payloadData.status) {
      case 'MESSAGE':
        if (userTabIsNotOpen(senderName)) {
          setMessageCount(prev => {
            const newMap = new Map(prev);
            let prevCount = newMap.get(senderName);

            let count = prevCount ? prevCount : 0;
            count++;
            newMap.set(senderName, count);

            return newMap;
          })

          setContacts(prev => {
            if (!prev.some(ctt => ctt.userName === senderName)) {
              buildContact(senderName).then((data) => {
                setContacts(contacts => [...contacts, data]);
              });
            }
            return prev;
          });

          setUnreadMessageCount(prev => ++prev);
          setAlert({ message: `${senderName} diz: ${payloadData.message}`, type: 'info' })

        } else {
          setChatMessages(prev => [...prev, payloadData]);

          setTab(tab => {
            let receiver = tab.userName;
            Api.put(`/api/v1/messages/mark_messages_as_read/${receiver}`)
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
            return tab;
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
  }, [sendPrivateMessagesRead, setChatMessages, setContacts,
    setMessageCount, setAlert, setUnreadMessageCount, setTab,
    userData, userTabIsNotOpen]);

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
    setAlert({ 
      message: tryGetErrorMessage(error), 
      type: 'error'
    });
  }, [setAlert]);

  const onConnected = useCallback((client) => {
    client.subscribe(`/user/${userData.userName}/private`, onPrivateMessageReceived);
    client.subscribe('/chatroom/public', onPublicMessageReceived);
    client.subscribe(`/user/${userData.userName}/errors`, onError);
  }, [onPrivateMessageReceived, onPublicMessageReceived, userData, onError])

  return {
    sendPrivateMessagesRead, onConnected
  }
}