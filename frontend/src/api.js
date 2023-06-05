import axios from 'axios';

const Auth = axios.create({
    baseURL: `http://${window.location.hostname}:8080/`
})

const Api = axios.create({
    baseURL: `http://${window.location.hostname}:8080/`
})

export { Auth, Api };