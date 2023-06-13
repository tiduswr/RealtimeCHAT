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

const AuthAlert = ({ title, message, type }) => {
  
    const isMobile = useMediaQuery('(max-width: 600px')
  
    return (
    <AlertStyled severity={type} sx={{ width: isMobile ? '80%' : '30%'}}>
      <AlertTitle>{title}</AlertTitle>
      {message}
    </AlertStyled>
  );
};

export default AuthAlert;