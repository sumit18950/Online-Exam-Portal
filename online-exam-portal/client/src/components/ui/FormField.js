export const FormField = ({
  id,
  label,
  error,
  className = '',
  children,
}) => {
  return (
    <div className={`ui-form-field ${className}`.trim()}>
      {label && <label htmlFor={id}>{label}</label>}
      {children}
      {error && <small className="ui-field-error">{error}</small>}
    </div>
  );
};

