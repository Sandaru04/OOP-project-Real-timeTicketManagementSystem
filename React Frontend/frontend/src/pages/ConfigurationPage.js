import React, { useEffect, useState } from 'react';
import { getAllConfigurations } from '../services/configurationService';
import ConfigurationForm from '../components/ConfigurationForm';

const ConfigurationPage = () => {
  const [configurations, setConfigurations] = useState([]);

  useEffect(() => {
    getAllConfigurations()
      .then((data) => setConfigurations(data))
      .catch((error) => console.error('Error fetching configurations:', error));
  }, []);

  return (
    <div>
      <h2>Configurations</h2>
      <ConfigurationForm />
      <ul>
        {configurations.map((config) => (
          <li key={config.id}>
            <p>{config.name}</p>
            <p>{config.totalTickets}</p>
            <p>{config.numberOfVendors}</p>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default ConfigurationPage;
