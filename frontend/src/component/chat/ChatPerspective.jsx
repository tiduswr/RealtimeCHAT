import React, { useContext, useState } from 'react'
import ChatContent from './ChatContent'
import MemberList from '../MemberList'
import { Context } from '../../pages/App'
import { useWebSockets } from '../../websocket/useWebSocket';

const ChatPerspective = () => {

    const { setMessageCount, messageCount, setUnreadMessageCount } = useContext(Context);
    const { closeMenu, isMenuOpen } = useContext(Context);
    const [chatMessages, setChatMessages] = useState([]);
    const [tab, setTab] = useState(null);
    const [contacts, setContacts] = useState([])
    const [connecting, sendPrivateMessagesRead, stompClient] = useWebSockets({ setTab, contacts, setContacts, setChatMessages });

    if (connecting) return null;
    
    return (
        <>
            <MemberList
                messageCount={messageCount}
                setMessageCount={setMessageCount}
                setUnreadMessageCount={setUnreadMessageCount}
                closeMenu={closeMenu}
                isMenuOpen={isMenuOpen}
                setTab={setTab}
                tab={tab}
                contacts={contacts}
                setContacts={setContacts}
                sendPrivateMessagesRead={sendPrivateMessagesRead}
            />
            <ChatContent
                setTab={setTab}
                tab={tab}
                stompClient={stompClient}
                chatMessages={chatMessages}
                setChatMessages={setChatMessages}
                setMessageCount={setMessageCount}
            />
        </>
    )
}

export default ChatPerspective