import { useContext } from 'react';
import { Link, NavLink, useNavigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';
import './Navbar.css';

const roleLinks = {
  ADMIN: [
    { to: '/admin-dashboard', label: 'Dashboard' },
    { to: '/admin/users', label: 'Users' },
    { to: '/admin/exams', label: 'Exams' },
    { to: '/admin/results', label: 'Results' },
  ],
  TEACHER: [
    { to: '/teacher-dashboard', label: 'Dashboard' },
    { to: '/teacher/exams', label: 'Exams' },
    { to: '/teacher/questions', label: 'Questions' },
    { to: '/teacher/results', label: 'Results' },
  ],
  STUDENT: [
    { to: '/student-dashboard', label: 'Dashboard' },
    { to: '/student/exams', label: 'Exams' },
    { to: '/student/results', label: 'Results' },
  ],
};

export const Navbar = () => {
  const { isAuthenticated, role, logout } = useContext(AuthContext);
  const navigate = useNavigate();
  const links = roleLinks[role] || [];

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <header className="navbar">
      <div className="navbar-container">
        <Link to="/" className="navbar-logo" aria-label="Exam Portal home">
          <span className="logo-mark">EP</span>
          Exam Portal
        </Link>

        <nav className="nav-links" aria-label="Primary navigation">
          {isAuthenticated ? (
            <>
              {links.map((item) => (
                <NavLink
                  key={item.to}
                  to={item.to}
                  className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}
                >
                  {item.label}
                </NavLink>
              ))}
              <NavLink to="/profile" className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}>
                Profile
              </NavLink>
              <span className="nav-role-badge">{role}</span>
              <button onClick={handleLogout} className="nav-link logout-btn">Logout</button>
            </>
          ) : (
            <>
              <NavLink to="/login" className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}>
                Login
              </NavLink>
              <NavLink to="/register" className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}>
                Register
              </NavLink>
            </>
          )}
        </nav>
      </div>
    </header>
  );
};
