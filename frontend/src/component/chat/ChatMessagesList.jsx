import React, { useEffect, useState, useCallback } from 'react';
import { Button, List, ListItem } from '@mui/material';
import KeyboardDoubleArrowUpIcon from '@mui/icons-material/KeyboardDoubleArrowUp';
import { Api } from '../../api';
import ChatMessages from './ChatMessages';
import { USER_SERVICE_URI } from '../../hostResolver';

const ChatMessagesList = ({ chatMessages, username, page, setPage, lengthPages }) => {
    const [usersData, setUsersData] = useState(new Map());
    const [loading, setLoading] = useState(true);
    const [bottomChat, setBottomChat] = useState(null);

    const userImageApi = useCallback(async (senderName) => {
        try {
            const res = await Api.get(`${USER_SERVICE_URI}/users/retrieve_profile_image/${senderName}`, { responseType: 'arraybuffer' });
            const imageUrl = URL.createObjectURL(new Blob([res.data], { type: 'image/png' }));
            return imageUrl;
        } catch (error) {
            return undefined;
        }
    }, []);

    const userFormalNameApi = useCallback(async (senderName) => {
        try {
            const res = await Api.get(`${USER_SERVICE_URI}/users/retrieve_profile_info/${senderName}`);
            return res.data.formalName;
        } catch (error) {
            return 'ERROR';
        }
    }, []);

    useEffect(() => {
        async function loadUsersData() {
            let changed = false;
            const usersNames = new Set();
            chatMessages.forEach((message) => {
                const sender = message.sender;

                if (!usersData.has(sender)) {
                    changed = true;
                    usersNames.add(sender);
                }
            });

            if(changed){
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
            }
            
            setLoading(false);
            if(page === 0 && bottomChat) 
                bottomChat.scrollIntoView({behavior: 'smooth'});
        };

        loadUsersData();
    }, [userImageApi, userFormalNameApi, chatMessages, usersData, bottomChat, page]);

    const retrieveMoreMessages = () => {
        setPage(prev => {
            const newPage = ++prev;
            return newPage < lengthPages ? newPage : prev;
        })
    }

    if (loading) {
        return null;
    }

    return (
        <List>
            <ListItem 
                key='pagination'
                sx={{
                    justifyContent: 'center'
                }}
            >
                {!!(page < (lengthPages - 1)) && 
                    <Button variant='contained' 
                        size='small'
                        onClick={retrieveMoreMessages}
                        endIcon={<KeyboardDoubleArrowUpIcon />}
                    >
                        Mais
                    </Button>
                }
            </ListItem>
            <ChatMessages 
                page={page}
                chatMessages={chatMessages}
                usersData={usersData} 
                username={username}
            />
            <div id='bottom_chat' ref={setBottomChat}></div>
        </ List>
    );
};

export default ChatMessagesList;