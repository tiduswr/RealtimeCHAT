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
import { styled } from '@mui/system';
import { AuthContext } from '../../contexts/AuthProvider';
import { useNavigate } from 'react-router-dom';
import { useParams } from "react-router-dom";

const Form = styled(Box)({
  mt: 30, 
  padding: 20, 
  display: 'flex',
  flexDirection: 'column',
  alignItems: 'center',
  width: '100%'
});

export default function ValidateRecoverPassword() {

  const { passwordCode } = useParams();
  const navigate = useNavigate();
  const { setAlert } = useContext(NotificationContext);
  const [passwordError, setPasswordError] = useState(undefined);
  const { isAuthenticated } = useContext(AuthContext);
  const [componentVisible, setComponentVisible] = useState(false);

  const formIsEmpty = (form) => {
    if (form.password1 === '' || form.password2 === '') {
      setPasswordError('As senhas são obrigatórias.');
    }

    return form.password1 === '' ||
    form.password2 === '';
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
      password1: data.get('password_01'),
      password2: data.get('password_02')
    };

    if (formIsEmpty(form)) return;
    if (passwordsNotMatch(form)) return;

    Auth.post(`/recover_password/validate/${passwordCode}`, {
      password: form.password1
    }).then((res) => {
      if (res.status === 200) {
        cleanFields();
        setAlert({type: 'success', message: 'Senha alterada'});
      }
    }).catch((error) => {
      const data = error.response?.data;
      if (data) {
        if (data.error) {
          const { password } = data.error;
          
          if (password) {
            const arr = password.split(',');
            setPasswordError(arr.join(', '));
          }else{
            setAlert({type: 'error', message: data.message !== '' ? data.message : "Erro no servidor"});
          }
        }else{
          setAlert({type: 'error', message: data.message});
        }
      } else {
        setAlert({type: 'error', message: 'O servidor não respondeu a solicitação.'});
      }
    })
  };

  const passwordsNotMatch = (form) => {
    const test = form.password1 !== form.password2;
    if (test) {
      setPasswordError('As senhas não conferem.');
    }
    return test;
  }

  const cleanFields = () => {
    document.getElementById('password_01').value = '';
    document.getElementById('password_02').value = '';
    setPasswordError(undefined);
  }

  const cleanPasswordsOnChange = (e) => {
    if (passwordError) {
      setPasswordError(undefined);
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
            Insira sua nova senha
          </Typography>
          <Form component="form" noValidate onSubmit={handleSubmit}>
            <Grid container spacing={2}>
            <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  error={passwordError ? true : false}
                  name="password_01"
                  label="Senha"
                  type="password"
                  id="password_01"
                  autoComplete="new-password"
                  onChange={cleanPasswordsOnChange}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  error={passwordError ? true : false}
                  helperText={passwordError ? passwordError : undefined}
                  name="password_02"
                  label="Repita a Senha"
                  type="password"
                  id="password_02"
                  autoComplete="new-password"
                  onChange={cleanPasswordsOnChange}
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