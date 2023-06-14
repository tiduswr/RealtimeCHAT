import React, { createContext, useState, useEffect, useContext } from 'react'
import { Api } from '../api';
import { AuthContext } from './AuthProvider';

const UserContext = createContext();

const UserProvider = ({ children }) => {

    const [userData, setUserData] = useState(null);
    const [userImage, setUserImage] = useState(null);
    const { isAuthenticated } = useContext(AuthContext);

    useEffect(() => {
        if(isAuthenticated){
            Api.get('/users/retrieve_profile_info')
            .then(res => {
                if (res.status === 200) {
                    const data = res.data;
                    setUserData(data);
                    Api.get(`/users/retrieve_profile_image/${data.userName}`, { responseType: 'arraybuffer' })
                        .then((res) => {
                            if (res.status === 200 && res.data) {
                                const imageUrl = URL.createObjectURL(new Blob([res.data], { type: 'image/png' }))
                                setUserImage(imageUrl);
                            }
                        })
                }
            })
        }
    }, [isAuthenticated])

    return (
        <UserContext.Provider value={{ userData, userImage }}>
            {children}
        </UserContext.Provider>
    );
}

export { UserContext, UserProvider }