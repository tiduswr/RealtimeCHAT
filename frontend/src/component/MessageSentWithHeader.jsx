import React from 'react';
import { Typography, Avatar, ListItemAvatar, ListItemText, Paper, Grid } from '@mui/material';

const MessageSent = ({ message, senderName, userImage }) => {
    
    return (
        <Paper elevation={1} 
            sx={{padding : '10px', 
                minWidth: '200px'
        }}>
            <Grid container direction='row' alignItems='center'>
                <Grid item>
                    <ListItemAvatar>
                        <Avatar alt={senderName} src={userImage} />
                    </ListItemAvatar>
                </Grid>
                <Grid item>
                    <Typography 
                        variant="caption" 
                        color="textPrimary" 
                        sx={{fontSize:'16px', 
                            fontWeight:'bold', 
                            wordBreak: 'break-word'
                        }}>{senderName}</Typography>
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

export default MessageSent;