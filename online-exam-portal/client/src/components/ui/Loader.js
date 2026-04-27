export const Loader = ({ text = 'Loading...' }) => {
  return (
    <div className="ui-loader-wrap" role="status" aria-live="polite">
      <div className="ui-loader" />
      <span>{text}</span>
    </div>
  );
};

