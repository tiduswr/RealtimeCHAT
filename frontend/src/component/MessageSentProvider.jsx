import React from 'react'
import MessageSent from './MessageSent';
import MessageSentWithHeaders from './MessageSentWithHeader';

const MessageSentProvider = ({ message, senderName, lastMessageSender, image, formalName }) => {

    const lastMessageIsFromTheSameUser = () =>{
        return !lastMessageSender || (lastMessageSender !== senderName);
    }

    return (
        <React.Fragment>
            {lastMessageIsFromTheSameUser() ?
                <MessageSentWithHeaders
                    message={message}
                    image={image}
                    formalName={formalName}
                    senderName={senderName}
                />
            : 
                <MessageSent 
                    message={message}
                />
            }
            
        </React.Fragment>
    )
}

export default MessageSentProvider