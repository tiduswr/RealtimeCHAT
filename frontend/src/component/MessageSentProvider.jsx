import React from 'react'
import MessageSent from './MessageSent';
import MessageSentWithHeaders from './MessageSentWithHeader';

const MessageSentProvider = ({ message, senderName, lastMessageSender }) => {

    const lastMessageIsFromTheSameUser = () =>{
        return !lastMessageSender || (lastMessageSender.sender !== senderName);
    }

    return (
        <React.Fragment>
            {lastMessageIsFromTheSameUser() ?
                <MessageSentWithHeaders
                    message={message}
                    senderName={senderName}
                />
            : 
                <MessageSent 
                    message={message}
                    senderName={senderName}
                />
            }
            
        </React.Fragment>
    )
}

export default MessageSentProvider