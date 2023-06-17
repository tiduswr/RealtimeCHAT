import axios from 'axios';
import moment from 'moment';

const baseURL = `http://${window.location.hostname}:8080/`;

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

  localStorage.removeItem('refresh_token');
  localStorage.removeItem('access_token');
};

Api.interceptors.request.use(
  async (config) => {
    const accessToken = localStorage.getItem('access_token');
    const accessTk = JSON.parse(accessToken);

    const isExpired = accessTk && moment(accessTk.expiration).isBefore(moment());

    if (isExpired) {
      await refreshToken();
    }

    const updatedAccessToken = localStorage.getItem('access_token');
    const updatedAccessTk = JSON.parse(updatedAccessToken);

    config.headers.Authorization = `Bearer ${updatedAccessTk?.jwtToken}`;
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export { Auth, Api, refreshToken };
