import React, { useContext } from 'react'
import ChatContent from './ChatContent'
import MemberList from '../MemberList'
import { Context } from '../../pages/App'

const ChatPerspective = ({ closeMenu, isMenuOpen, stompClient, chatMessages, setChatMessages, tab, setTab, sendPrivateMessagesRead }) => {

    const { setMessageCount, messageCount, setUnreadMessageCount } = useContext(Context);

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
                sendPrivateMessagesRead={sendPrivateMessagesRead}
            />
            <ChatContent
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