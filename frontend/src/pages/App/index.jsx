import React, { createContext, useCallback, useContext, useEffect, useState } from 'react';

import { Api } from '../../api';
import ChatRoom from '../../component/chat/ChatRoom';
import Header from '../../component/Header';
import { AuthContext } from '../../contexts/AuthProvider';
import { UserContext } from '../../contexts/UserProvider';

const Context = createContext();

const App = () => {
  const [unreadMessagesCount, setUnreadMessageCount] = useState(0);
  const [isMenuOpen, setMenuOpen] = useState(false);
  const [messageCount, setMessageCount] = useState(new Map());
  const [showAlert, setShowAlert] = useState({ visible: false, sender: '' });
  const { authLoading } = useContext(AuthContext);
  const { userLoading } = useContext(UserContext);
  console.log('updated')
  useEffect(() => {
    if (showAlert.visible) {
      setTimeout(() => {
        setShowAlert({ ...showAlert, visible: false });
      }, 3000);
    }
  }, [showAlert]);

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
        console.log(error);
      }
    }

    countUnreadedMessages();
  }, [messageCount])

  if (authLoading || userLoading) return null;

  return (
    <Context.Provider
      value={{
        closeMenu,
        isMenuOpen,
        messageCount,
        showAlert,
        setShowAlert,
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
