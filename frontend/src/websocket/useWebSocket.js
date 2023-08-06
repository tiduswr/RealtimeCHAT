import { useContext, useEffect, useState } from 'react';
import SockJS from 'sockjs-client';
import { over } from 'stompjs';

import { baseURL, handleTokenRefreshRequest } from '../api';
import { UserContext } from '../contexts/UserProvider';

import { useWebsocketMessagesConfig } from './useWebsocketMessagesConfig';
import { NotificationContext } from '../contexts/NotificationProvider';
import { tryGetErrorMessage } from '../errorParser';

export const useWebSockets = ({ setTab, setContacts, setChatMessages }) => {
  const { userData } = useContext(UserContext);
  const [connecting, setConnecting] = useState(true);
  const [stompClient, setStompClient] = useState(null);
  const { setAlert } = useContext(NotificationContext);

  const {
    sendPrivateMessagesRead,
    onConnected
  } = useWebsocketMessagesConfig({ setTab, setContacts, setChatMessages, setStompClient });

  useEffect(() => {
    const connectToStompServer = async () => {
      setConnecting(true);
      try {
        const ACCESS_TOKEN = await handleTokenRefreshRequest();

        if (ACCESS_TOKEN) {
          const sock = new SockJS(`${baseURL}ws/`);
          setStompClient(prev => {
            const client = over(sock, { heartbeatIncoming: 4000, heartbeatOutgoing: 4000 });

            // client.debug = null;
            client.connect(
              { 'Authorization': `Bearer ${ACCESS_TOKEN.jwtToken}` }, () => onConnected(client),
              (error) => {
                setAlert({ 
                  title: 'Erro na conexão WebSocket!', 
                  message: tryGetErrorMessage(error), 
                  type: 'error', 
                  show: true, 
                  wrap: false
                });
              })

            return client;
          });
        }
      } catch (error) {
        setAlert({ 
          title: 'Erro na conexão WebSocket!', 
          message: tryGetErrorMessage(error), 
          type: 'error', 
          show: true, 
          wrap: false
        });
      }
      setConnecting(false);
    }

    if (userData) {
      setStompClient(prev => {
        if (!prev) connectToStompServer();
        return prev;
      })
    }
    return () => {
      setStompClient(prev => {
        if (prev) {
          if (prev.connected) prev.disconnect();
          return null;
        }
        return prev;
      })

    };
  }, [userData, onConnected, setAlert]);

  return [connecting, sendPrivateMessagesRead, stompClient];
}