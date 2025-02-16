import axios from 'axios';

const API_URL = 'http://localhost:8080/api/ticketing-system/';

export const getPoolSize = async () => {
  try {
    const response = await axios.get(`${API_URL}polling/poolSize`);
    return response.data;
  } catch (error) {
    console.error('Error fetching pool size:', error);
  }
};

export const isSystemRunning = async () => {
  try {
    const response = await axios.get(`${API_URL}polling/isRunning`);
    return response.data;
  } catch (error) {
    console.error('Error checking system status:', error);
  }
};
