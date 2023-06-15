import axios from 'axios';
import moment from 'moment';

let refreshTokenPromise = undefined;

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
  if (refreshTokenPromise) {
    return refreshTokenPromise;
  }

  refreshTokenPromise = Auth.post('/refresh_token', {
    refreshToken: JSON.parse(localStorage.getItem('refresh_token')).jwtToken,
  });

  try {
    const res = await refreshTokenPromise;

    if (res.status === 200) {
      const { refreshToken, token } = res.data;

      if (refreshToken?.jwtToken && token?.jwtToken) {
        localStorage.setItem('refresh_token', JSON.stringify(refreshToken));
        localStorage.setItem('access_token', JSON.stringify(token));
      }
      console.log('refreshed :D');
    } else {
      if (localStorage.getItem('refresh_token')) {
        Auth.post('/quit', {
          refreshToken: JSON.parse(localStorage.getItem('refresh_token')).jwtToken,
        });
      }

      localStorage.removeItem('refresh_token');
      localStorage.removeItem('access_token');
    }

    refreshTokenPromise = undefined;
  } catch (error) {
    if (localStorage.getItem('refresh_token')) {
      Auth.post('/quit', {
        refreshToken: JSON.parse(localStorage.getItem('refresh_token')).jwtToken,
      });
    }

    localStorage.removeItem('refresh_token');
    localStorage.removeItem('access_token');

    refreshTokenPromise = undefined;
  }
};

Api.interceptors.request.use(
  async (config) => {
    const accessToken = localStorage.getItem('access_token');
    let accessTk = JSON.parse(accessToken);

    const isExpired = accessTk && moment(accessTk.expiration).isBefore(moment());

    if (isExpired) {
      accessTk = await refreshToken();
    }

    config.headers.Authorization = `Bearer ${accessTk?.jwtToken}`;
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export { Auth, Api, refreshToken };
