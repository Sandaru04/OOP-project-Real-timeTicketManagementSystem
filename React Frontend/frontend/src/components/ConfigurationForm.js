import React, { useState } from 'react';
import { addConfiguration } from '../services/configurationService';

const ConfigurationForm = () => {
  const [config, setConfig] = useState({
    name: '',
    totalTickets: 0,
    numberOfVendors: 0,
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setConfig((prevConfig) => ({ ...prevConfig, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    addConfiguration(config)
      .then((response) => {
        alert('Configuration added successfully');
        setConfig({ name: '', totalTickets: 0, numberOfVendors: 0 });
      })
      .catch((error) => alert('Error adding configuration:', error));
  };

  return (
    <form onSubmit={handleSubmit}>
      <input
        type="text"
        name="name"
        value={config.name}
        onChange={handleChange}
        placeholder="Configuration Name"
      />
      <input
        type="number"
        name="totalTickets"
        value={config.totalTickets}
        onChange={handleChange}
        placeholder="Total Tickets"
      />
      <input
        type="number"
        name="numberOfVendors"
        value={config.numberOfVendors}
        onChange={handleChange}
        placeholder="Number of Vendors"
      />
      <button type="submit">Add Configuration</button>
    </form>
  );
};

export default ConfigurationForm;
