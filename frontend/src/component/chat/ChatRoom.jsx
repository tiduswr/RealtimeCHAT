import React, { useContext } from 'react';
import { Container } from '@mui/material';
import MessageReceivedAlert from '../message/MessageReceivedAlert';
import { Context } from '../../pages/App/index.jsx';
import ChatPerspective from './ChatPerspective';

const ChatRoom = () => {
  const {
    showAlert
  } = useContext(Context);

  return (
    <React.Fragment>
      <Container maxWidth="xl">
        <ChatPerspective />
      </Container>
      {showAlert.visible && <MessageReceivedAlert senderName={showAlert.sender} />}
    </React.Fragment>
  );
};

export default ChatRoom;
