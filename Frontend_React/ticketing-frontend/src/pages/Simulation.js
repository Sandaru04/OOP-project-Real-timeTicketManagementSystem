// src/pages/Simulation.jsx
import { useState, useEffect, useRef } from 'react';
import { connectWebSocket, disconnectWebSocket } from '../websocket';

function Simulation() {
  const [simulationEvents, setSimulationEvents] = useState([]);
  const [isRunning, setIsRunning] = useState(false);
  const [configs, setConfigs] = useState([]);
  const [selectedConfigId, setSelectedConfigId] = useState('');
  const logContainerRef = useRef(null);

  useEffect(() => {
    const fetchConfigs = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/configs/getAll');
        const data = await response.json();
        setConfigs(data || []);
        if (data && data.length > 0) {
          setSelectedConfigId(data[0].id.toString());
        }
      } catch (error) {
        console.error('Error fetching configurations:', error);
      }
    };

    fetchConfigs();

    connectWebSocket((message) => {
      console.log('Frontend received:', message); // Debug incoming messages
      const lines = message.split('\n').filter(line => line.trim() !== '');
      setSimulationEvents((prev) => {
        const newEvents = [...prev, ...lines].slice(-200);
        console.log('Updated events:', newEvents); // Debug state update
        return newEvents;
      });
    });

    return () => disconnectWebSocket();
  }, []);

  useEffect(() => {
    if (logContainerRef.current) {
      logContainerRef.current.scrollTop = logContainerRef.current.scrollHeight;
    }
  }, [simulationEvents]);

  const handleStart = async () => {
    if (!selectedConfigId) return;
    try {
      const response = await fetch(`http://localhost:8080/api/ticketing-system/start/${selectedConfigId}`, {
        method: 'POST',
      });
      if (response.ok) {
        setIsRunning(true);
        setSimulationEvents([`Simulation started with config ID: ${selectedConfigId}`]);
      } else {
        setSimulationEvents((prev) => [...prev, 'Failed to start simulation']);
      }
    } catch (error) {
      console.error('Error starting simulation:', error);
      setSimulationEvents((prev) => [...prev, 'Error starting simulation']);
    }
  };

  const handleStop = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/ticketing-system/stop', {
        method: 'POST',
      });
      if (response.ok) {
        setIsRunning(false);
        setSimulationEvents((prev) => [...prev, 'Simulation stopped manually']);
      } else {
        setSimulationEvents((prev) => [...prev, 'Failed to stop simulation']);
      }
    } catch (error) {
      console.error('Error stopping simulation:', error);
      setSimulationEvents((prev) => [...prev, 'Error stopping simulation']);
    }
  };

  const selectedConfig = configs.find((config) => config.id.toString() === selectedConfigId);

  return (
    <div className="container">
      <h1 className="page-title">Real-time Simulation</h1>
      <div className="select-container">
        <label htmlFor="configSelect">Select Configuration:</label>
        <select
          id="configSelect"
          value={selectedConfigId}
          onChange={(e) => setSelectedConfigId(e.target.value)}
          disabled={isRunning}
        >
          {configs.map((config) => (
            <option key={config.id} value={config.id}>
              ID {config.id}
            </option>
          ))}
        </select>
      </div>
      <div style={{ marginBottom: '1.5rem' }}>
        <button
          onClick={handleStart}
          disabled={isRunning || !selectedConfigId}
          className="button start"
        >
          Start
        </button>
        <button
          onClick={handleStop}
          disabled={!isRunning}
          className="button stop"
        >
          Stop
        </button>
      </div>
      <div className="simulation-log">
        <h3 style={{ fontSize: '1.2rem', marginBottom: '1rem' }}>Simulation Events</h3>
        <div className="log-container" ref={logContainerRef}>
          {simulationEvents.length > 0 ? (
            simulationEvents.map((event, index) => (
              <p key={index} className="log-entry">{event}</p>
            ))
          ) : (
            <p className="log-entry">No events yet. Start the simulation to see real-time activity.</p>
          )}
        </div>
      </div>
      {selectedConfig && (
        <div className="config-display">
          <h3 style={{ fontSize: '1.2rem', marginBottom: '1rem' }}>
            Selected Configuration (ID: {selectedConfig.id})
          </h3>
          <p>Number of Vendors: <span>{selectedConfig.numberOfVendors}</span></p>
          <p>Total Tickets: <span>{selectedConfig.totalTickets}</span></p>
          <p>Ticket Release Rate: <span>{selectedConfig.ticketReleaseRate}ms</span></p>
          <p>Number of Customers: <span>{selectedConfig.numberOfCustomers}</span></p>
          <p>Customer Retrieval Rate: <span>{selectedConfig.customerRetrievalRate}ms</span></p>
          <p>Max Ticket Capacity: <span>{selectedConfig.maxTicketCapacity}</span></p>
        </div>
      )}
    </div>
  );
}

export default Simulation;