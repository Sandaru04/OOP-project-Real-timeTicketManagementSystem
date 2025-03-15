function TicketCard({ title, value }) {
    return (
      <div className="ticket-card">
        <h3 className="ticket-card-title">{title}</h3>
        <p className="ticket-card-value">{value}</p>
      </div>
    );
  }
  
  export default TicketCard;