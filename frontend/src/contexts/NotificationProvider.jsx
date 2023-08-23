import { createContext} from "react";
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const NotificationContext = createContext();
const TIME_IN_SCREEN = 3000;

const NotificationProvider = ({ children }) => {

    const setAlert = (obj) =>{
        if (!document.hidden) {
            toast[obj.type](obj?.message ? obj.message : "");
        }
    }

    return (
        <NotificationContext.Provider value={{setAlert}}>
            { children }
            <ToastContainer
                position="top-center"
                autoClose={TIME_IN_SCREEN}
                hideProgressBar={false}
                newestOnTop={false}
                closeOnClick
                rtl={false}
                pauseOnFocusLoss={false}
                draggable
                pauseOnHover
                theme="light"
            />
        </NotificationContext.Provider>
    );

}

export { NotificationProvider, NotificationContext }