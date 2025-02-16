import React from 'react';
import { Link } from 'react-router-dom';

const Header = () => {
  return (
    <header>
      <h1>Ticketing System</h1>
      <nav>
        <Link to="/">Home</Link>
        <Link to="/configurations">Configurations</Link>
        <Link to="/ticketing-system">Ticketing System</Link>
        <Link to="/polling">Polling</Link>
      </nav>
    </header>
  );
};

export default Header;
