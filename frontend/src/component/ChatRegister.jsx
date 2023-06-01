import { Button, Grid, TextField, Container, Paper, Typography } from '@mui/material'
import React from 'react'

const ChatRegister = ({ username, handleUsername, registerUser }) => {

    return (
        <React.Fragment>
            <Container maxWidth='xs' style={{ display: 'flex', alignItems: 'center', height: '80vh'}}>
                <Paper elevation={3} style={{ padding: '40px' }}>
                    <Grid container spacing={2} columns={12}  alignItems="center">
                        <Grid>
                            <Typography variant='h5'>
                                Chat WebSocket
                            </Typography>
                        </Grid>
                        <Grid item xs={8}>
                            <TextField
                                id="user-name"
                                label="Coloque seu nome"
                                name="user-name"
                                value={username}
                                onChange={handleUsername}
                                variant="outlined"
                                fullWidth
                            />
                        </Grid>
                        <Grid item xs={4} justifyContent='center'>
                            <Button variant="contained" fullWidth onClick={registerUser}>
                                Login
                            </Button>
                        </Grid>
                    </Grid>
                </Paper>
            </Container>
        </React.Fragment>
    )
}

export default ChatRegister