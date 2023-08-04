import React, { useEffect, useState, useCallback } from 'react';
import MessageSentProvider from '../message/MessageSentProvider';
import { List, ListItem } from '@mui/material';
import { Api } from '../../api';

const ChatMessagesList = ({ chatMessages, username }) => {
    const [usersData, setUsersData] = useState(new Map());
    const [loading, setLoading] = useState(true);

    const lastMessageIsFromOtherUser = (lastSender, sender) => {
        return lastSender !== null && lastSender === sender;
    };

    const userImageApi = useCallback(async (senderName) => {
        try {
            const res = await Api.get(`/users/retrieve_profile_image/${senderName}`, { responseType: 'arraybuffer' });
            const imageUrl = URL.createObjectURL(new Blob([res.data], { type: 'image/png' }));
            return imageUrl;
        } catch (error) {
            return undefined;
        }
    }, []);

    const userFormalNameApi = useCallback(async (senderName) => {
        try {
            const res = await Api.get(`/users/retrieve_profile_info/${senderName}`);
            return res.data.formalName;
        } catch (error) {
            return 'ERROR';
        }
    }, []);

    useEffect(() => {
        async function loadUsersData() {
            const usersNames = new Set();
            chatMessages.forEach((message) => {
                const sender = message.sender;

                if (!usersData.has(sender)) {
                    usersNames.add(sender);
                }
            });

            const usersDataPromises = Array.from(usersNames).map(async (un) => {
                const image = await userImageApi(un);
                const formalName = await userFormalNameApi(un);
                return [un, { image, formalName }];
            });

            const usersDataArray = await Promise.all(usersDataPromises);
            if (usersDataArray.length !== 0) {
                setUsersData(prevUsersData => {
                    const updatedUsersData = new Map(prevUsersData);

                    usersDataArray.forEach(el => {
                        updatedUsersData.set(el[0], el[1]);
                    })

                    return updatedUsersData;
                })
            }
            setLoading(false);
        };

        loadUsersData();
    }, [userImageApi, userFormalNameApi, chatMessages, usersData]);

    if (loading) {
        return null;
    }

    return (
        <List>
            {chatMessages.map((chat, index) => {
                const sender = chat.sender;
                const userData = usersData.get(sender);

                return (
                    <ListItem
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
            })}
        </List>
    );
};

export default ChatMessagesList;
