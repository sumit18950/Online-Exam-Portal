export const Card = ({ title, subtitle, className = '', children, actions }) => {
  return (
    <section className={`ui-card ${className}`.trim()}>
      {(title || subtitle || actions) && (
        <header className="ui-card-header">
          <div>
            {title && <h2>{title}</h2>}
            {subtitle && <p>{subtitle}</p>}
          </div>
          {actions && <div>{actions}</div>}
        </header>
      )}
      {children}
    </section>
  );
};

