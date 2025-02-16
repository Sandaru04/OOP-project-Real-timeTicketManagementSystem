import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import HomePage from './pages/HomePage';
import ConfigurationPage from './pages/ConfigurationPage';
import TicketingSystemPage from './pages/TicketingSystemPage';
import PollingPage from './pages/PollingPage';
import Header from './components/Header';
import Sidebar from './components/Sidebar';

function App() {
  return (
    <Router>
      <Header />
      <Sidebar />
      <div className="main-content">
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/configurations" element={<ConfigurationPage />} />
          <Route path="/ticketing-system" element={<TicketingSystemPage />} />
          <Route path="/polling" element={<PollingPage />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
