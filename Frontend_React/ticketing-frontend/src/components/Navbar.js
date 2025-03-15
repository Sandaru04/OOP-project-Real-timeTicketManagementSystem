import { NavLink } from 'react-router-dom';

function Navbar() {
  return (
    <nav className="navbar">
      <div className="navbar-container">
        <h1 className="navbar-title">Ticketing System</h1>
        <div className="navbar-links">
          <NavLink to="/" className={({ isActive }) => isActive ? "active" : ""}>
            Home
          </NavLink>
          <NavLink to="/ticket-pool" className={({ isActive }) => isActive ? "active" : ""}>
            Ticket Pool
          </NavLink>
          <NavLink to="/simulation" className={({ isActive }) => isActive ? "active" : ""}>
            Simulation
          </NavLink>
          <NavLink to="/configuration" className={({ isActive }) => isActive ? "active" : ""}>
            Configuration
          </NavLink>
        </div>
      </div>
    </nav>
  );
}

export default Navbar;