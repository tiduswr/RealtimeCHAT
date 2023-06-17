import React, { useContext } from 'react'
import MessageSent from './MessageSent';
import MessageSentWithHeaders from './MessageSentWithHeader';
import { UserContext } from '../../contexts/UserProvider'
import { format } from 'date-fns';

const MessageSentProvider = ({ message, senderName, lastMessageSender, image, formalName, read, date }) => {

    const { userData } = useContext(UserContext);
    const formattedDate = format(new Date(date), 'dd/MM/yyyy HH:mm:ss');

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
                    read={read}
                    date={formattedDate}
                    ownUsername={userData.userName}
                />
            : 
                <MessageSent 
                    message={message}
                    read={read}
                    date={formattedDate}
                    senderName={senderName}
                    ownUsername={userData.userName}
                />
            }
            
        </React.Fragment>
    )
}

export default MessageSentProvider