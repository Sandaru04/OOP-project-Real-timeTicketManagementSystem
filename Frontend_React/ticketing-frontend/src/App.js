import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar';
import Home from './pages/Home';
import TicketPool from './pages/TicketPool';
import Simulation from './pages/Simulation';
import Configuration from './pages/Configuration';  // Import the new page

function App() {
  return (
    <Router>
      <div>
        <Navbar />
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/ticket-pool" element={<TicketPool />} />
          <Route path="/simulation" element={<Simulation />} />
          <Route path="/configuration" element={<Configuration />} />  // Add new route
        </Routes>
      </div>
    </Router>
  );
}

export default App;