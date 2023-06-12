import React from 'react';
import { Typography, ListItemText, Paper, Grid } from '@mui/material';
import AvatarWithInitial from './AvatarWithInitial'

const MessageSentWithHeader = ({ message, senderName, image, formalName }) => {

    return (
        <Paper elevation={1} 
            sx={{padding : '10px', 
                minWidth: '200px',
                paddingTop: '10px'
        }}>
            <Grid container direction='row' alignItems='center'>
                <Grid item>
                    <AvatarWithInitial 
                        senderName={formalName} 
                        image={image} 
                    />
                </Grid>
                <Grid item>
                    <Typography 
                        variant="caption" 
                        color="textPrimary" 
                        sx={{fontSize:'16px', 
                            fontWeight:'bold', 
                            wordBreak: 'break-word'
                        }}>{formalName}</Typography>
                </Grid>
            </Grid>
            <ListItemText
                primary={
                    <Typography variant="body2" sx={{ wordBreak: 'break-word', overflowWrap: 'break-word', paddingTop:'10px' }}>
                    {message}
                    </Typography>
                }
            />
        </Paper>
    );
  };

export default MessageSentWithHeader;