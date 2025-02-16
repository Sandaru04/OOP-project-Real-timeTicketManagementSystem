import axios from 'axios';

const API_URL = 'http://localhost:8080/api/configs/';

export const getAllConfigurations = async () => {
  try {
    const response = await axios.get(`${API_URL}getAll`);
    return response.data;
  } catch (error) {
    console.error('Error fetching configurations:', error);
  }
};

export const addConfiguration = async (config) => {
  try {
    const response = await axios.post(`${API_URL}add`, config);
    return response.data;
  } catch (error) {
    console.error('Error adding configuration:', error);
  }
};
