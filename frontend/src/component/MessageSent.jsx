import React from 'react';
import { ListItemText, Paper, Typography } from '@mui/material';

const MessageSent = ({ message }) => {

    return (
        <Paper elevation={1} 
            sx={{padding : '10px', 
                minWidth: '200px'
        }}>
            <ListItemText
                primary={
                    <Typography variant="body2" sx={{ wordBreak: 'break-word', overflowWrap: 'break-word' }}>
                    {message}
                    </Typography>
                }
            />
        </Paper>
      
    );
  };

export default MessageSent;