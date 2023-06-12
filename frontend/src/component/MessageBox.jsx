import React from 'react'
import { TextField, InputAdornment, IconButton } from '@mui/material'
import SendIcon from '@mui/icons-material/Send';
import { useState } from 'react';

const MessageBox = ({ sendMessage, placeholder, username }) => {

    const [ message, setMessage ] = useState('');

    const handleKeyDown = (event) => {
        if (event.key === 'Enter') {
            prepareAndSendMessage();
        }
    };

    const prepareAndSendMessage = () => {
        setMessage(prev => {
            sendMessage(prev, username);
            return '';
        })
    }

    const handleMessageTeste = e => {
        const { value } = e.target;
        setMessage(value)
    } 

    return (
        <TextField
            type="text"
            name="message"
            label={message === '' ? placeholder : ''}
            value={message}
            onChange={handleMessageTeste}
            onKeyDown={handleKeyDown}
            variant="outlined"
            fullWidth
            sx={{
                '.MuiOutlinedInput-root': {
                borderRadius: '50px',
                }
            }}
            InputLabelProps={{
                shrink: false
            }}
            InputProps={{
                endAdornment: (
                <InputAdornment position="end">
                    <IconButton onClick={prepareAndSendMessage} edge="end">
                        <SendIcon />
                    </IconButton>
                </InputAdornment>
                ),
            }}
        />
    )
}

export default MessageBox