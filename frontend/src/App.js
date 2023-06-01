import React, { useEffect, useState, useCallback } from 'react';
import ChatRoom from './component/ChatRoom';
import Header from './component/Header';
import { createTheme, ThemeProvider } from '@mui/material';

const theme = createTheme({
  typography: {
    fontFamily: 'Roboto, sans-serif',
  },
});

const App = () => {
  const [unreadMessagesCount, setUnreadMessageCount] = useState(0);
  const [isMenuOpen, setMenuOpen] = useState(false);
  const [messageCount, setMessageCount] = useState(new Map());
  const [showAlert, setShowAlert] = useState({ visible: false, sender: '' });
  const [publicChats, setPublicChats] = useState([]);
  const [privateChats, setPrivateChats] = useState(new Map());
  const [tab, setTab] = useState('CHATROOM');
  const [userData, setUserData] = useState({
    username: '',
    recievername: '',
    connected: false,
    message: '',
  });

  //Sugerido pelo compilador
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
      setPrivateChats((prevPrivChats) => {
        const updatedPrivateChats = new Map(prevPrivChats);

        if (updatedPrivateChats.has(tab)) {
          const messages = prevPrivChats.get(tab);
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
    setUserData(prevUD => {
      if(prevUD.connected){
        setMenuOpen(!isMenuOpen);
      }
      return prevUD;
    })
  };

  const closeMenu = () => {
    setMenuOpen(false);
  };

  return (
    <ThemeProvider theme={theme}>
      <Header
        toggleMenu={toggleMenu}
        closeMenu={closeMenu}
        isMenuOpen={isMenuOpen}
        unreadMessagesCount={unreadMessagesCount}
      />
      <ChatRoom
        toggleMenu={toggleMenu}
        closeMenu={closeMenu}
        isMenuOpen={isMenuOpen}
        updateUnreadMessageCount={updateUnreadMessageCount}
        messageCount={messageCount}
        showAlert={showAlert}
        publicChats={publicChats}
        privateChats={privateChats}
        tab={tab}
        userData={userData}
        setPrivateChats={setPrivateChats}
        setTab={setTab}
        setShowAlert={setShowAlert}
        setMessageCount={setMessageCount}
        setUserData={setUserData}
        setPublicChats={setPublicChats}
      />
    </ThemeProvider>
  );
};

export default App;
