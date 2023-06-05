import React, { useEffect, useState, useCallback, createContext } from 'react';
import ChatRoom from '../../component/ChatRoom';
import Header from '../../component/Header';

const Context = createContext();

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
    },[]
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
    
      <Context.Provider
        value={{ 
          toggleMenu, closeMenu, isMenuOpen, unreadMessagesCount,
          updateUnreadMessageCount, messageCount, showAlert,
          publicChats, privateChats, tab, userData, setPrivateChats,
          setTab, setShowAlert, setMessageCount, setUserData, setPublicChats
        }}>

        <Header/>
        <ChatRoom/>

      </Context.Provider>

  );
};

export { App, Context };
