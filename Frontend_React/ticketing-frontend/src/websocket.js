import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';

let stompClient = null;

export const connectWebSocket = (onMessageReceived) => {
  const socket = new SockJS('http://localhost:8080/websocket');
  stompClient = Stomp.over(socket);
  
  stompClient.connect({}, () => {
    stompClient.subscribe('/topic/simulation', (message) => {
      const data = JSON.parse(message.body);
      onMessageReceived(data);
    });
  });
};

export const disconnectWebSocket = () => {
  if (stompClient) {
    stompClient.disconnect();
  }
};