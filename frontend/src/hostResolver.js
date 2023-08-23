const resolveHost = () => {
    if (process.env.REACT_APP_BACKEND_HOST) {
      return `${process.env.REACT_APP_BACKEND_HOST}${process.env.REACT_APP_BACKEND_URI}`;
    } else {
      // For Docker and Kubernetes enviroments
      const port = window.location.port ||
        (window.location.protocol === 'https:' ? '443' : '80');
      return `${window.location.protocol}//${window.location.hostname}:${port}/backend/`;
    }
  }

const AUTH_SERVICE_URI = process.env.REACT_APP_BACKEND_TYPE === "MICROSSERVICE" ? "apis/auth/v1" : "";
const USER_SERVICE_URI = process.env.REACT_APP_BACKEND_TYPE === "MICROSSERVICE" ? "apis/user/v1" : "";
const MESSAGE_SERVICE_URI = process.env.REACT_APP_BACKEND_TYPE === "MICROSSERVICE" ? "apis/message/v1" : "";

const websocketPrivateSubscribtionResolver = (username) => {
  const isMicrosservices = process.env.REACT_APP_BACKEND_TYPE === "MICROSSERVICE";
  return isMicrosservices ? `/topic/private.${username}` : `/user/${username}/private`;
}

const websocketPublicSubscribtionResolver = () => {
  const isMicrosservices = process.env.REACT_APP_BACKEND_TYPE === "MICROSSERVICE";
  return isMicrosservices ? '/topic/chatroom.public' : '/chatroom/public';
}

const websocketErrorSubscribtionResolver = (username) => {
  const isMicrosservices = process.env.REACT_APP_BACKEND_TYPE === "MICROSSERVICE";
  return isMicrosservices ? `/topic/errors.${username}` : `/user/${username}/errors`;
}

export {
  resolveHost, AUTH_SERVICE_URI,
  USER_SERVICE_URI, MESSAGE_SERVICE_URI,
  websocketPrivateSubscribtionResolver, websocketPublicSubscribtionResolver,
  websocketErrorSubscribtionResolver
}