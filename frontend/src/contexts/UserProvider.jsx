import React, { createContext, useState, useEffect, useContext, useCallback } from 'react'
import { Api } from '../api';
import { AuthContext } from './AuthProvider';

const UserContext = createContext();

const UserProvider = ({ children }) => {

    const [userData, setUserData] = useState(null);
    const [userImage, setUserImage] = useState(null);
    const [userDataLoading, setUserDataLoading] = useState(true);
    const { isAuthenticated } = useContext(AuthContext);

    useEffect(() => {
        if(isAuthenticated){
            setUserDataLoading(true);
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
                                setUserDataLoading(false);
                            }
                        }).catch(e => {
                            setUserDataLoading(false);
                        })
                }
            }).catch(e => {
                setUserDataLoading(false);
            })
        }
    }, [isAuthenticated])

    const updateFormalName = useCallback(() => {
        if(userData){
            setUserDataLoading(true);
            Api.get('/users/retrieve_profile_info')
            .then(res => {
                if (res.status === 200) {
                    const data = res.data;
                    setUserData(data);
                    setUserDataLoading(false);
                }
            }).catch(e => {
                setUserDataLoading(false);
            })
        }
    }, [userData])

    const updateImage = useCallback(() => {
        if(isAuthenticated && userData){
            setUserDataLoading(true);
            Api.get(`/users/retrieve_profile_image/${userData.userName}`, { responseType: 'arraybuffer' })
            .then((res) => {
                if (res.status === 200 && res.data) {
                    const imageUrl = URL.createObjectURL(new Blob([res.data], { type: 'image/png' }))
                    setUserImage(imageUrl);
                    setUserDataLoading(false);
                }
            }).catch(e => {
                setUserDataLoading(false);
            })
        }
    }, [userData, isAuthenticated])

    return (
        <UserContext.Provider value={{ userData, userImage, updateImage, updateFormalName, userDataLoading }}>
            {children}
        </UserContext.Provider>
    );
}

export { UserContext, UserProvider }