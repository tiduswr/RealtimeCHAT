import { Alert, AlertTitle } from '@mui/material';
import { styled } from '@mui/system';
import React from 'react';
import { useMediaQuery } from '@mui/material';

const AlertStyled = styled(Alert)(({ theme }) => ({
  display: 'flex',
  alignItems: 'center',
  width: '30%',
  position: 'fixed',
  top: theme.spacing(2),
  right: theme.spacing(2)
}));

const MessageReceivedAlert = ({ senderName }) => {

  const isMobile = useMediaQuery('(max-width: 600px')

  return (
    <AlertStyled severity="info" sx={{ width: isMobile ? '80%' : '30%' }}>
      <AlertTitle>Mensagem recebida!</AlertTitle>
      {`${senderName} acaba de te enviar uma mensagem.`}
    </AlertStyled>
  );
};

export default MessageReceivedAlert;
