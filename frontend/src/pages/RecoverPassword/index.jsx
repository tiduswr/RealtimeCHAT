import React, { useContext, useState, useEffect } from 'react';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import PublicHeader from '../../component/PublicHeader';
import { Auth } from '../../api';
import { NotificationContext } from '../../contexts/NotificationProvider';
import { resolveBaseHost } from '../../hostResolver';
import { osName, browserName } from 'react-device-detect';
import { styled } from '@mui/system';
import { AuthContext } from '../../contexts/AuthProvider';
import { useNavigate } from 'react-router-dom';

const Form = styled(Box)({
  mt: 30, 
  padding: 20, 
  display: 'flex',
  flexDirection: 'column',
  alignItems: 'center',
  width: '100%'
});

export default function RecoverPassword() {

  const navigate = useNavigate();
  const { setAlert } = useContext(NotificationContext);
  const [emailError, setEmailError] = useState(undefined);
  const { isAuthenticated } = useContext(AuthContext);
  const [componentVisible, setComponentVisible] = useState(false);

  const formIsEmpty = (form) => {
    if(form.email === ''){
      setEmailError('O email é obrigatório.')
    }

    return form.email === '';
  }

  useEffect(() => {
    if (isAuthenticated) {
      navigate('/chat');
    } else {
      setComponentVisible(true);
    }
  }, [isAuthenticated, navigate]);

  if (!componentVisible) {
    return null;
  }

  const handleSubmit = (event) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    const form = {
      email: data.get('email')
    };

    if (formIsEmpty(form)) return;

    Auth.post('/recover_password', {
      email: form.email,
      redirectPrefix: `${resolveBaseHost()}/validate/recover-password/`,
      browserName: browserName,
      operatingSystem: osName
    }).then((res) => {
      cleanFields();
      setAlert({type: 'info', message: 'Solicitação enviada pro email, caso ele exista!'});
    }).catch((error) => {
      const data = error.response?.data;
      if (data) {
        if (data.error) {
          const { email } = data.error;

          if(email){
            setEmailError(email);
          }else{
            setAlert({type: 'error', message: data.message !== '' ? data.message : "Erro no servidor"});
          }
        }
      } else {
        setAlert({type: 'error', message: 'O servidor não respondeu a solicitação.'});
      }
    })
  };

  const cleanFields = () => {
    document.getElementById('email').value = '';
    setEmailError(undefined);
  }

  const cleanEmailOnChange = (e) => {
    if (emailError) {
      setEmailError(undefined);
    }
  }

  return (
    <>
      <PublicHeader/>
      <Container component="main" maxWidth="xs">
        <CssBaseline />
        <Box
          sx={{
            marginTop: 8,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center'
          }}
        >
          <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
            <LockOutlinedIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            Solicitar Recuperação de Senha
          </Typography>
          <Form component="form" noValidate onSubmit={handleSubmit}>
            <Grid container spacing={2}>
              <Grid item xs={15}>
                <TextField
                  required
                  fullWidth
                  id="email"
                  error={emailError ? true : false}
                  helperText={emailError ? emailError : undefined}
                  label="E-Mail"
                  name="email"
                  autoComplete="email"
                  onChange={cleanEmailOnChange}
                />
              </Grid>
            </Grid>
            <Button
              type="submit"
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
            >
              Enviar
            </Button>
          </Form>
        </Box>
      </Container>
    </>
  );
}