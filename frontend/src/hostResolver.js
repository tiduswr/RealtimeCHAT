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

export {
  resolveHost
}