import React from 'react';
import { Container, Typography, Grid, Paper, Button } from '@mui/material';
import PublicHeader from '../../component/PublicHeader';
import HomeImage from '../../static/home_poster.jpeg';
import styled from '@emotion/styled';
import { useNavigate } from 'react-router-dom';

const ChatButton = styled(Button)({
  marginTop: '10px',
  fontWeight: 'bold',
  fontSize: '20px',
});

function Home() {
  const navigate = useNavigate();
  
  return (
    <>
      <PublicHeader />
      <Container component="main" maxWidth="lg" sx={{ marginTop: '2rem', marginBottom: '2rem'}}>
        <Paper sx={{ padding: '10px' }}>
          <Grid container spacing={2} alignItems="center">
            <Grid item xs={12} sm={6}>
              <img src={HomeImage} alt="Imagem de exemplo" style={{ width: '100%' }} />
            </Grid>
            <Grid item xs={12} sm={6} >
              <Typography variant="h4" gutterBottom>
                Bem-vindo ao Chat em Tempo Real com WebSocket e Spring Boot!
              </Typography>
              <Typography variant="body1">
                Este site é uma plataforma de bate-papo em tempo real, desenvolvida com tecnologia WebSocket e impulsionada pelo servidor Spring Boot.
                Desfrute de uma experiência de comunicação instantânea com outros usuários em salas de bate-papo.
              </Typography>
              <Typography variant="body1">
                Com este aplicativo, você pode enviar e receber mensagens instantâneas em tempo real,
                permitindo uma comunicação eficaz e dinâmica com pessoas de todo o mundo.
              </Typography>
              <Grid sx={{ display: 'flex', justifyContent: 'flex-end', textAlign: 'right', alignItems: 'center' }}>
                <ChatButton variant='outlined' color='primary' onClick={e => navigate('/chat')}>Acessar CHAT</ChatButton>
              </Grid>
            </Grid>
          </Grid>
        </Paper>
      </Container>
    </>
  );
}

export default Home;
