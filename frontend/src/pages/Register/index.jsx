import React, { useContext, useState } from 'react';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import Link from '@mui/material/Link';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import PublicHeader from '../../component/PublicHeader';
import { Auth } from '../../api';
import { NotificationContext } from '../../contexts/NotificationProvider';

export default function Register() {

  const { setAlert } = useContext(NotificationContext);
  const [passwordError, setPasswordError] = useState(undefined);
  const [userNameError, setUserNameError] = useState(undefined);
  const [formalNameError, setFormalNameError] = useState(undefined);

  const formIsEmpty = (form) => {
    if (form.userName === '') {
      setUserNameError('O nome de usuário é obrigatório.')
    }
    if (form.formalName === '') {
      setFormalNameError('O nome de é obrigatório.')
    }
    if (form.password1 === '' || form.password2 === '') {
      setPasswordError('As senhas são obrigatórias.');
    }

    return form.userName === '' ||
      form.password1 === '' ||
      form.password2 === '' ||
      form.formalName === '';
  }

  const passwordsNotMatch = (form) => {
    const test = form.password1 !== form.password2;
    if (test) {
      setPasswordError('As senhas não conferem.');
    }
    return test;
  }

  const handleSubmit = (event) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    const form = {
      userName: data.get('userName'),
      password1: data.get('password_01'),
      password2: data.get('password_02'),
      formalName: data.get('formalName')
    };

    if (formIsEmpty(form)) return;
    if (passwordsNotMatch(form)) return;

    Auth.post('/signup', {
      userName: form.userName,
      password: form.password1,
      formalName: form.formalName
    }).then((res) => {
      if (res.status === 201) {
        cleanFields();
        setAlert({type: 'success', message: 'Cadastro realizado'});
      }
    }).catch((error) => {
      const data = error.response?.data;

      if (data) {
        if (data.error) {
          const { userName, password } = data.error;

          if (userName) {
            setUserNameError(userName);
          }else if (password) {
            const arr = password.split(',');
            setPasswordError(arr.join(', '));
          }else{
            setAlert({type: 'error', message: data.message});
          }
        }
      } else {
        setAlert({type: 'error', message: 'O servidor não respondeu a solicitação.'});
      }
    })
  };

  const cleanFields = () => {
    document.getElementById('formalName').value = '';
    document.getElementById('password_01').value = '';
    document.getElementById('password_02').value = '';
    document.getElementById('userName').value = '';
    setUserNameError(undefined);
    setFormalNameError(undefined);
    setPasswordError(undefined);
  }

  const cleanPasswordsOnChange = (e) => {
    if (passwordError) {
      setPasswordError(undefined);
    }
  }

  const cleanUserNameOnChange = (e) => {
    if (userNameError) {
      setUserNameError(undefined);
    }
  }

  const cleanFormalNameOnChange = (e) => {
    if (formalNameError) {
      setFormalNameError(undefined);
    }
  }

  return (
    <>
      <PublicHeader hideLoginButton={true} />
      <Container component="main" maxWidth="xs">
        <CssBaseline />
        <Box
          sx={{
            marginTop: 8,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
          }}
        >
          <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
            <LockOutlinedIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            Fazer cadastro
          </Typography>
          <Box component="form" noValidate onSubmit={handleSubmit} sx={{ mt: 3 }}>
            <Grid container spacing={2}>
              <Grid item xs={12}>
                <TextField
                  autoComplete="given-name"
                  name="formalName"
                  required
                  fullWidth
                  error={formalNameError ? true : false}
                  helperText={formalNameError ? formalNameError : undefined}
                  id="formalName"
                  label="Nome Pessoal"
                  autoFocus
                  onChange={cleanFormalNameOnChange}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  id="userName"
                  error={userNameError ? true : false}
                  helperText={userNameError ? userNameError : undefined}
                  label="Username"
                  name="userName"
                  autoComplete="userName"
                  onChange={cleanUserNameOnChange}
                />
              </Grid>
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
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
            >
              Cadastrar
            </Button>
            <Grid container justifyContent="flex-end">
              <Grid item>
                <Link href="/login" variant="body2">
                  Ja possui uma conta? Logar-se
                </Link>
              </Grid>
            </Grid>
          </Box>
        </Box>
      </Container>
    </>
  );
}