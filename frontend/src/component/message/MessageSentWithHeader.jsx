import React from 'react';
import { Typography, ListItemText, Paper, Grid, Box, Tooltip } from '@mui/material';
import AvatarWithInitial from '../AvatarWithInitial'
import CheckIcon from '@mui/icons-material/Check';
import DoneAllIcon from '@mui/icons-material/DoneAll';


const MessageSentWithHeader = ({ message, senderName, image, formalName, read, date, ownUsername }) => {

    return (
        <Tooltip title={date} placement='top'>
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
                {ownUsername === senderName &&
                    <Box sx={{ display: 'flex', justifyContent: 'flex-end', alignItems: 'flex-end' }}>
                        {read ? 
                            <DoneAllIcon sx={{ fontSize: 14 }} />
                        :
                            <CheckIcon sx={{ fontSize: 14 }} />
                        }
                    </Box>
                }
            </Paper>
        </Tooltip>
    );
  };

export default MessageSentWithHeader;