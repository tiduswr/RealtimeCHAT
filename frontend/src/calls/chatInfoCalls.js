import { Api } from '../api';
import { USER_SERVICE_URI, MESSAGE_SERVICE_URI } from '../hostResolver';

const fetchUserImage =
  async (username) => {
    const imageResponse = await Api.get(
      `${USER_SERVICE_URI}/users/retrieve_profile_image/${username}`,
      { responseType: 'arraybuffer' });
    return URL.createObjectURL(
      new Blob([imageResponse?.data], { type: 'image/png' }));
  }

const fetchUnreadedMessageCount =
  async (username) => {
    const res = await Api.get(`${MESSAGE_SERVICE_URI}/messages/retrieve_count/by/${username}`);
    return res?.data.count;
  }

const fetchUserInfo =
  async (username) => {
    const response = await Api.get(`${USER_SERVICE_URI}/users/retrieve_profile_info/${username}`);
    return response.data;
  }

const buildContact =
  async (username) => {
    const image = await fetchUserImage(username);
    const data = await fetchUserInfo(username);
    return {
      ...data, image
    }
  }

export {
  fetchUnreadedMessageCount, fetchUserImage, fetchUserInfo, buildContact
}