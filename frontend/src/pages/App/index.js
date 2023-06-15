import React, { useEffect, useState, useCallback, createContext, useContext } from 'react';
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
  const [publicChats, setPublicChats] = useState([]);
  const [privateChats, setPrivateChats] = useState(new Map());
  const [tab, setTab] = useState('CHATROOM');
  const { authLoading } = useContext(AuthContext);
  const { userLoading } = useContext(UserContext);

  const updateUnreadMessageCount = useCallback(
    (messageCount) => {
      let newCount = 0;
      messageCount.forEach((count) => {
        newCount += count;
      });
      setUnreadMessageCount(newCount);
    },
    []
  );

  useEffect(() => {
    if (tab !== 'CHATROOM') {
      setPrivateChats((prevPrivateChats) => {
        const updatedPrivateChats = new Map(prevPrivateChats);

        if (updatedPrivateChats.has(tab)) {
          const messages = prevPrivateChats.get(tab);
          const updatedMessages = messages.map((message) => ({
            ...message,
            read: true,
          }));
          updatedPrivateChats.set(tab, updatedMessages);
        }
        return updatedPrivateChats;
      });

      setMessageCount((prevMessageCount) => {
        const updatedMessageCount = new Map(prevMessageCount);

        if (updatedMessageCount.has(tab)) {
          updatedMessageCount.set(tab, 0);
        }
        return updatedMessageCount;
      });

      updateUnreadMessageCount(messageCount);
    }
  }, [tab, privateChats, messageCount, updateUnreadMessageCount]);

  useEffect(() => {
    if (showAlert.visible) {
      setTimeout(() => {
        setShowAlert({ ...showAlert, visible: false });
      }, 3000);
    }
  }, [showAlert]);

  const toggleMenu = () => {
    setMenuOpen((prevIsMenuOpen) => !prevIsMenuOpen);
  };

  const closeMenu = () => {
    setMenuOpen(false);
  };

  if(authLoading || userLoading) return null;

  return (
    <Context.Provider
      value={{
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
