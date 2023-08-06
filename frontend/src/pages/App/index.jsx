import React, { createContext, useCallback, useContext, useEffect, useState } from 'react';

import { Api } from '../../api';
import ChatRoom from '../../component/chat/ChatRoom';
import Header from '../../component/Header';
import { AuthContext } from '../../contexts/AuthProvider';
import { UserContext } from '../../contexts/UserProvider';
import { NotificationContext } from '../../contexts/NotificationProvider';
import { tryGetErrorMessage } from '../../errorParser';

const Context = createContext();

const App = () => {
  const [unreadMessagesCount, setUnreadMessageCount] = useState(0);
  const [isMenuOpen, setMenuOpen] = useState(false);
  const [messageCount, setMessageCount] = useState(new Map());
  const { authLoading } = useContext(AuthContext);
  const { userLoading } = useContext(UserContext);
  const { setAlert } = useContext(NotificationContext);

  console.log('updated')

  const toggleMenu = useCallback(() => {
    setMenuOpen((prevIsMenuOpen) => !prevIsMenuOpen);
  }, []);

  const closeMenu = useCallback(() => {
    setMenuOpen(false);
  }, []);

  useEffect(() => {
    const countUnreadedMessages = async () => {
      try {
        const res = await Api.get('/api/v1/messages/retrieve_count/total');
        const data = res.data.count;
        setUnreadMessageCount(parseInt(data));
      } catch (error) {
        setAlert({ 
          title: 'Erro ao contar mensagens não lidas!', 
          message: tryGetErrorMessage(error), 
          type: 'error', 
          show: true, 
          wrap: false
        });
      }
    }

    countUnreadedMessages();
  }, [messageCount, setAlert])

  if (authLoading || userLoading) return null;

  return (
    <Context.Provider
      value={{
        closeMenu,
        isMenuOpen,
        messageCount,
        setMessageCount,
        setUnreadMessageCount
      }}
    >
      <Header
        unreadMessagesCount={unreadMessagesCount}
        toggleMenu={toggleMenu}
      />
      <ChatRoom />
    </Context.Provider>
  );
};

export { App, Context };
