import { useState, useEffect } from 'react';

function TicketPool() {
  const [configs, setConfigs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const configResponse = await fetch('http://localhost:8080/api/configs/getAll');
        const configData = await configResponse.json();
        setConfigs(configData || []);
      } catch (error) {
        console.error('Error fetching configurations:', error);
        setError('Failed to load configurations. Please try again.');
      } finally {
        setLoading(false);
      }
    };

    fetchData();
    // Optional: Keep polling if you want real-time config updates
    const interval = setInterval(fetchData, 5000); // Poll every 5 seconds
    return () => clearInterval(interval);
  }, []);

  if (loading) {
    return <div className="container">Loading...</div>;
  }

  if (error) {
    return <div className="container">{error}</div>;
  }

  return (
    <div className="container">
      <h1 className="page-title">Ticket Pool Configurations</h1>
      {configs.length > 0 ? (
        <table className="config-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Details</th>
            </tr>
          </thead>
          <tbody>
            {configs.map((config) => (
              <tr key={config.id}>
                <td>{config.id}</td>
                <td>
                  Vendors: {config.numberOfVendors}, 
                  Total Tickets: {config.totalTickets}, 
                  Release Rate: {config.ticketReleaseRate}ms, 
                  Customers: {config.numberOfCustomers}, 
                  Retrieval Rate: {config.customerRetrievalRate}ms, 
                  Max Capacity: {config.maxTicketCapacity}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      ) : (
        <p>No configurations available.</p>
      )}
    </div>
  );
}

export default TicketPool;