import React, { createContext, useState, useEffect, useContext, useCallback } from 'react'
import { Api } from '../api';
import { AuthContext } from './AuthProvider';
import { USER_SERVICE_URI } from '../hostResolver';

const UserContext = createContext();

const UserProvider = ({ children }) => {

    const [userData, setUserData] = useState(null);
    const [userImage, setUserImage] = useState(null);
    const [userDataLoading, setUserDataLoading] = useState(false);
    const { isAuthenticated } = useContext(AuthContext);

    useEffect(() => {
        if (isAuthenticated) {
            setUserDataLoading(true);
            Api.get(`${USER_SERVICE_URI}/users/retrieve_profile_info`)
                .then(res => {
                    if (res.status === 200) {
                        const data = res.data;
                        setUserData(data);
                        Api.get(`${USER_SERVICE_URI}/users/retrieve_profile_image/${data.userName}`, { responseType: 'arraybuffer' })
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

    const updateUserData = useCallback(() => {
        if (userData) {
            setUserDataLoading(true);
            Api.get(`${USER_SERVICE_URI}/users/retrieve_profile_info`)
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

    const updateImage = () => {
        if (isAuthenticated && userData) {
            setUserDataLoading(true);
            Api.get(`${USER_SERVICE_URI}/users/retrieve_profile_image/${userData.userName}`, { responseType: 'arraybuffer' })
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
    }

    if(userDataLoading) return null;

    return (
        <UserContext.Provider value={{ setUserData, userData, userImage, updateImage, updateUserData, userDataLoading }}>
            {children}
        </UserContext.Provider>
    );
}

export { UserContext, UserProvider }