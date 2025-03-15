function Home() {
    return (
      <div 
        className="home"
        style={{ backgroundImage: "url('/background.jpg')" }}
      >
        <div className="home-content">
          <h1 className="home-title">Welcome to Ticketing System</h1>
          <p className="home-text">
            Monitor and manage your ticket distribution system in real-time
          </p>
          <div>
            <a href="/ticket-pool" className="home-button pool">
              View Ticket Pool
            </a>
            <a href="/simulation" className="home-button simulation">
              Start Simulation
            </a>
          </div>
        </div>
      </div>
    );
  }
  
  export default Home;