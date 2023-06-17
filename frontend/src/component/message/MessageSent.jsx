import React from 'react';
import { Box, ListItemText, Paper, Typography, Tooltip } from '@mui/material';
import CheckIcon from '@mui/icons-material/Check';
import DoneAllIcon from '@mui/icons-material/DoneAll';

const MessageSent = ({ message, read, date, ownUsername, senderName }) => {

    return (
        <Tooltip title={date} placement='top'>
            <Paper elevation={1}
                sx={{
                    padding: '10px',
                    minWidth: '200px'
                }}>
                <ListItemText
                    primary={
                        <>
                            <Typography variant="body2" sx={{ wordBreak: 'break-word', overflowWrap: 'break-word' }}>
                                {message}
                            </Typography>
                        </>
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

export default MessageSent;
