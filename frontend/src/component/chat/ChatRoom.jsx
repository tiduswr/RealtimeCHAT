import React from 'react';
import { Container } from '@mui/material';
import ChatPerspective from './ChatPerspective';

const ChatRoom = () => {
  return (
    <React.Fragment>
      <Container maxWidth="xl">
        <ChatPerspective />
      </Container>
    </React.Fragment>
  );
};

export default ChatRoom;
