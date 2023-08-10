import { useRef, useEffect } from 'react';
import MessageSentProvider from '../message/MessageSentProvider';
import { ListItem } from '@mui/material';

const ChatMessages = ({chatMessages, usersData, username}) => {

    const firstElementRef = useRef(null);
    const prevFirstElementRef = useRef(null);

    useEffect(() => {
        if(prevFirstElementRef.current){
            const element = prevFirstElementRef.current;
            element.scrollIntoView();
        }
        prevFirstElementRef.current = firstElementRef.current;
    }, [chatMessages]);

    const lastMessageIsFromOtherUser = (lastSender, sender) => {
        return lastSender !== null && lastSender === sender;
    };

    return chatMessages.map((chat, index) => {
        const sender = chat.sender;
        const userData = usersData.get(sender);

        return (
            <ListItem
                ref={index === 0 ? firstElementRef : null}
                key={chat.id}
                sx={{
                    display: 'flex',
                    justifyContent: chat.sender === username ? 'flex-end' : 'flex-start',
                    padding: 1,
                    paddingLeft: chat.sender === username ? '10%' : '0%',
                    paddingRight: chat.sender === username ? '0%' : '10%',
                    paddingTop: !lastMessageIsFromOtherUser(
                        index > 0 ? chatMessages[index - 1].sender : null,
                        chat.sender
                    )
                        ? '10px'
                        : '1px',
                    paddingBottom: '1px',
                }}
            >
                <MessageSentProvider
                    message={chat.message}
                    senderName={chat.sender}
                    image={userData?.image}
                    formalName={userData?.formalName}
                    lastMessageSender={index > 0 ? chatMessages[index - 1].sender : null}
                    read={chat.read}
                    date={chat.createdAt}
                />
            </ListItem>
        );
    })
}

export default ChatMessages;