import { render, screen } from '@testing-library/react';
import App from './App';

jest.mock('react-router-dom', () => {
  const React = require('react');

  const RouterMock = ({ children }) => <div>{children}</div>;
  const LinkMock = ({ children, to, ...rest }) => <a href={to} {...rest}>{children}</a>;

  return {
    BrowserRouter: RouterMock,
    Routes: ({ children }) => <>{children}</>,
    Route: ({ element }) => element,
    Navigate: ({ to }) => <span>redirect:{to}</span>,
    Link: LinkMock,
    NavLink: ({ children, to, className, ...rest }) => {
      const classValue = typeof className === 'function' ? className({ isActive: false }) : className;
      return <a href={to} className={classValue} {...rest}>{children}</a>;
    },
    useNavigate: () => jest.fn(),
    useParams: () => ({}),
    useLocation: () => ({ state: null }),
  };
}, { virtual: true });

test('renders exam portal branding', () => {
  render(<App />);
  expect(screen.getByText(/Run assessments with a modern, reliable exam portal/i)).toBeInTheDocument();
});
