export const Alert = ({ type = 'error', children }) => {
  if (!children) return null;
  return <div className={`ui-alert ui-alert-${type}`}>{children}</div>;
};

