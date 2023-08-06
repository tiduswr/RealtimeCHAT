import { createContext, useEffect, useState } from "react";
import Notification from '../component/Notification'

const NotificationContext = createContext();
const TIME_IN_SCREEN = 3000;
const defaultAlert = { 
    title: 'default', 
    message: 'default', 
    type: 'info', 
    show: false, 
    wrap: false
};

const NotificationProvider = ({ children }) => {
    const [alert, setAlert] = useState({...defaultAlert})

    useEffect(() => {
        if (alert.show) {
            setTimeout(() => {
                setAlert({...defaultAlert});
            }, TIME_IN_SCREEN);
        }
    }, [alert]);

    return (
        <NotificationContext.Provider value={{alert, setAlert}}>
            { children }
            {alert.show &&
                <Notification message={alert.message} type={alert.type} title={alert.title} />
            }
        </NotificationContext.Provider>
    );

}

export { NotificationProvider, NotificationContext }