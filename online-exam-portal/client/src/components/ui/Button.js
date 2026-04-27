export const Button = ({
  children,
  type = 'button',
  variant = 'primary',
  fullWidth = false,
  className = '',
  ...props
}) => {
  return (
    <button
      type={type}
      className={`ui-btn ui-btn-${variant} ${fullWidth ? 'ui-btn-full' : ''} ${className}`.trim()}
      {...props}
    >
      {children}
    </button>
  );
};

