import { useContext } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';
import './Navbar.css';

export const Navbar = () => {
  const { isAuthenticated, role, logout } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const renderRoleLinks = () => {
    switch (role) {
      case 'ADMIN':
        return (
          <>
            <Link to="/admin-dashboard" className="nav-link">Dashboard</Link>
            <Link to="/admin/users" className="nav-link">Users</Link>
            <Link to="/admin/subjects" className="nav-link">Subjects</Link>
            <Link to="/admin/exams" className="nav-link">Exams</Link>
            <Link to="/admin/questions" className="nav-link">Questions</Link>
            <Link to="/admin/results" className="nav-link">Results</Link>
          </>
        );
      case 'TEACHER':
        return (
          <>
            <Link to="/teacher-dashboard" className="nav-link">Dashboard</Link>
            <Link to="/teacher/subjects" className="nav-link">Subjects</Link>
            <Link to="/teacher/exams" className="nav-link">Exams</Link>
            <Link to="/teacher/questions" className="nav-link">Questions</Link>
            <Link to="/teacher/results" className="nav-link">Results</Link>
          </>
        );
      case 'STUDENT':
        return (
          <>
            <Link to="/student-dashboard" className="nav-link">Dashboard</Link>
            <Link to="/student/exams" className="nav-link">View Exams</Link>
            <Link to="/student/results" className="nav-link">My Results</Link>
          </>
        );
      default:
        return null;
    }
  };

  return (
    <nav className="navbar">
      <div className="navbar-container">
        <Link to="/" className="navbar-logo">Online Exam Portal</Link>
        <div className="nav-links">
          {isAuthenticated ? (
            <>
              {renderRoleLinks()}
              <Link to="/profile" className="nav-link">Profile</Link>
              <span className="nav-role-badge">{role}</span>
              <button onClick={handleLogout} className="nav-link logout-btn">Logout</button>
            </>
          ) : (
            <>
              <Link to="/login" className="nav-link">Login</Link>
              <Link to="/register" className="nav-link">Register</Link>
            </>
          )}
        </div>
      </div>
    </nav>
  );
};
