import React, { useEffect, useState } from 'react';
import { getPoolSize, isSystemRunning } from '../services/ticketingSystemService';

const PollingPage = () => {
  const [poolSize, setPoolSize] = useState(0);
  const [isRunning, setIsRunning] = useState(false);

  useEffect(() => {
    getPoolSize().then((data) => setPoolSize(data['Pool Size']));
    isSystemRunning().then((status) => setIsRunning(status));
  }, [poolSize, isRunning]);

  return (
    <div>
      <h2>Polling</h2>
      <p>Pool Size: {poolSize}</p>
      <p>System Running: {isRunning ? 'Yes' : 'No'}</p>
    </div>
  );
};

export default PollingPage;
