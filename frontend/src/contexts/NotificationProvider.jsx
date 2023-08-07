import { createContext} from "react";
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const NotificationContext = createContext();
const TIME_IN_SCREEN = 3000;

const NotificationProvider = ({ children }) => {

    const setAlert = (obj) =>{
        toast[obj.type](obj?.message ? obj.message : "Eita");
    }

    return (
        <NotificationContext.Provider value={{setAlert}}>
            { children }
            <ToastContainer
                position="top-right"
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