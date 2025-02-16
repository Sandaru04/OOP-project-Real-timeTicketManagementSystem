import React, { useState } from 'react';

const TicketingSystemPage = () => {
  const [status, setStatus] = useState('stopped');

  const handleStartSystem = () => {
    // Call your backend API to start the ticketing system
    setStatus('running');
  };

  const handleStopSystem = () => {
    // Call your backend API to stop the ticketing system
    setStatus('stopped');
  };

  return (
    <div>
      <h2>Ticketing System</h2>
      <p>Status: {status}</p>
      <button onClick={handleStartSystem}>Start System</button>
      <button onClick={handleStopSystem}>Stop System</button>
    </div>
  );
};

export default TicketingSystemPage;
