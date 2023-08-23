import { useContext, useEffect, useState } from 'react';
import SockJS from 'sockjs-client';
import { over } from 'stompjs';

import { baseURL, handleTokenRefreshRequest } from '../api';
import { UserContext } from '../contexts/UserProvider';

import { useWebsocketMessagesConfig } from './useWebsocketMessagesConfig';
import { NotificationContext } from '../contexts/NotificationProvider';
import { tryGetErrorMessage } from '../errorParser';
import { MESSAGE_SERVICE_URI } from '../hostResolver';

const RECONECT_DELAY = 5000;

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
          const sock = new SockJS(`${baseURL}${MESSAGE_SERVICE_URI === "" ? "" : `${MESSAGE_SERVICE_URI}/`}ws/`);
          
          setStompClient(prev => {
            const client = over(sock, {});

            //client.debug = null;

            client.connect(
              { 'Authorization': `Bearer ${ACCESS_TOKEN.jwtToken}` }, () => onConnected(client),
              (error) => {
                let errorMessage = error?.body ? error.body : error;

                if(errorMessage.startsWith("Whoops! Lost connection")){
                  errorMessage = "Oops! Conexão perdida, tentando reconectar..."
                  setTimeout(async () => {
                    
                    await connectToStompServer();
                  }, RECONECT_DELAY);
                }

                setAlert({message: errorMessage, type: 'error'});
            })

            return client;
          });
        }
      } catch (error) {
        setAlert({message: tryGetErrorMessage(error), type: 'error'});
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
      if (stompClient && stompClient.connected) stompClient.disconnect();
    };
  }, [userData, onConnected, setAlert, stompClient]);

  return [connecting, sendPrivateMessagesRead, stompClient];
}