import { Api } from '../api';

const fetchUserImage =
  async (username) => {
    const imageResponse = await Api.get(
      `/users/retrieve_profile_image/${username}`,
      { responseType: 'arraybuffer' });
    return URL.createObjectURL(
      new Blob([imageResponse?.data], { type: 'image/png' }));
  }

const fetchUnreadedMessageCount =
  async (username) => {
    const res = await Api.get(`/api/v1/messages/retrieve_count/by/${username}`);
    return res?.data.count;
  }

const fetchUserInfo =
  async (username) => {
    const response = await Api.get(`/users/retrieve_profile_info/${username}`);
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