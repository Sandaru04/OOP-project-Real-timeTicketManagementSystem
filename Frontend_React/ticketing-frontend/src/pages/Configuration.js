import { useState } from 'react';

function Configuration() {
  const [config, setConfig] = useState({
    numberOfVendors: '',
    totalTickets: '',
    ticketReleaseRate: '',
    numberOfCustomers: '',
    customerRetrievalRate: '',
    maxTicketCapacity: '',
  });
  const [message, setMessage] = useState('');

  const handleChange = (e) => {
    const { name, value } = e.target;
    setConfig((prevConfig) => ({
      ...prevConfig,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch('http://localhost:8080/api/configs/add', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          numberOfVendors: parseInt(config.numberOfVendors),
          totalTickets: parseInt(config.totalTickets),
          ticketReleaseRate: parseInt(config.ticketReleaseRate),
          numberOfCustomers: parseInt(config.numberOfCustomers),
          customerRetrievalRate: parseInt(config.customerRetrievalRate),
          maxTicketCapacity: parseInt(config.maxTicketCapacity),
        }),
      });

      if (response.ok) {
        setMessage('Configuration updated successfully!');
        setConfig({
          numberOfVendors: '',
          totalTickets: '',
          ticketReleaseRate: '',
          numberOfCustomers: '',
          customerRetrievalRate: '',
          maxTicketCapacity: '',
        });
      } else {
        setMessage('Failed to update configuration. Please try again.');
      }
    } catch (error) {
      console.error('Error updating configuration:', error);
      setMessage('An error occurred. Please try again.');
    }
  };

  return (
    <div className="container">
      <h1 className="page-title">Update Configuration</h1>
      <form onSubmit={handleSubmit} className="config-form">
        <div className="form-group">
          <label htmlFor="numberOfVendors">Number of Vendors:</label>
          <input
            type="number"
            id="numberOfVendors"
            name="numberOfVendors"
            value={config.numberOfVendors}
            onChange={handleChange}
            required
            min="1"
          />
        </div>
        <div className="form-group">
          <label htmlFor="totalTickets">Total Tickets:</label>
          <input
            type="number"
            id="totalTickets"
            name="totalTickets"
            value={config.totalTickets}
            onChange={handleChange}
            required
            min="1"
          />
        </div>
        <div className="form-group">
          <label htmlFor="ticketReleaseRate">Ticket Release Rate (ms):</label>
          <input
            type="number"
            id="ticketReleaseRate"
            name="ticketReleaseRate"
            value={config.ticketReleaseRate}
            onChange={handleChange}
            required
            min="1"
          />
        </div>
        <div className="form-group">
          <label htmlFor="numberOfCustomers">Number of Customers:</label>
          <input
            type="number"
            id="numberOfCustomers"
            name="numberOfCustomers"
            value={config.numberOfCustomers}
            onChange={handleChange}
            required
            min="1"
          />
        </div>
        <div className="form-group">
          <label htmlFor="customerRetrievalRate">Customer Retrieval Rate (ms):</label>
          <input
            type="number"
            id="customerRetrievalRate"
            name="customerRetrievalRate"
            value={config.customerRetrievalRate}
            onChange={handleChange}
            required
            min="1"
          />
        </div>
        <div className="form-group">
          <label htmlFor="maxTicketCapacity">Max Ticket Capacity:</label>
          <input
            type="number"
            id="maxTicketCapacity"
            name="maxTicketCapacity"
            value={config.maxTicketCapacity}
            onChange={handleChange}
            required
            min="1"
          />
        </div>
        <button type="submit" className="button submit">Update Configuration</button>
      </form>
      {message && <p className="message">{message}</p>}
    </div>
  );
}

export default Configuration;