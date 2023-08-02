import axios from 'axios';
import moment from 'moment';
import { resolveHost } from './hostResolver'

let refreshing = false;
let refreshPromise = null;
const baseURL = resolveHost();

axios.defaults.headers.common = {
  'Content-Type': 'application/json',
};

const Auth = axios.create({
  baseURL: baseURL,
});

const Api = axios.create({
  baseURL: baseURL,
});

const refreshToken = async () => {
  try {
    const refreshToken = JSON.parse(localStorage.getItem('refresh_token'));
    const response = await Auth.post('/refresh_token', {
      refreshToken: refreshToken.jwtToken,
    });

    if (response.status === 200) {
      const { refreshToken, token } = response.data;

      if (refreshToken?.jwtToken && token?.jwtToken) {
        localStorage.setItem('refresh_token', JSON.stringify(refreshToken));
        localStorage.setItem('access_token', JSON.stringify(token));
        refreshing = false;
        refreshPromise = null;
      }
      console.log('Token refreshed successfully');
    } else {
      handleTokenRefreshError();
    }
  } catch (error) {
    handleTokenRefreshError();
  }
};

const handleTokenRefreshError = () => {
  if (localStorage.getItem('refresh_token')) {
    Auth.post('/quit', {
      refreshToken: JSON.parse(localStorage.getItem('refresh_token')).jwtToken,
    });
  }

  refreshing = false;
  refreshPromise = null;
  localStorage.removeItem('refresh_token');
  localStorage.removeItem('access_token');
};

const handleTokenRefreshRequest = async () => {
  const accessToken = localStorage.getItem('access_token');
  const accessTk = JSON.parse(accessToken);

  const isExpired = accessTk && moment(accessTk.expiration).isBefore(moment());

  if (isExpired) {
    if(!refreshing){
      refreshing = true;
      refreshPromise = refreshToken();
    }
    await refreshPromise;
  }

  const updatedAccessToken = localStorage.getItem('access_token');
  return JSON.parse(updatedAccessToken);
}

Api.interceptors.request.use(
  async (config) => {
    const updatedAccessToken = await handleTokenRefreshRequest()
    config.headers.Authorization = `Bearer ${updatedAccessToken?.jwtToken}`;
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export { Auth, Api, refreshToken, handleTokenRefreshRequest, baseURL };
