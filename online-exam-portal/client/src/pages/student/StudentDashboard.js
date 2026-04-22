import { useContext } from 'react';
import { Link } from 'react-router-dom';
import { AuthContext } from '../../context/AuthContext';
import { BookOpen, BarChart, User, Key } from '../../components/Icons';
import './Student.css';

export const StudentDashboard = () => {
  const { user } = useContext(AuthContext);

  return (
    <div className="container">
      <div className="dashboard-card">
        <h2>Student Dashboard</h2>
        <p className="welcome-msg">Welcome, {user?.username || 'Student'}!</p>
        <p className="role-label">Role: STUDENT</p>

        <div className="dashboard-actions">
          <Link to="/student/exams" className="action-card">
            <span className="action-icon"><BookOpen style={{ width: '1.3rem', height: '1.3rem', fill: 'currentColor' }} /></span>
            <h3>View Exams</h3>
            <p>Browse and attempt available exams</p>
          </Link>
          <Link to="/student/results" className="action-card">
            <span className="action-icon"><BarChart style={{ width: '1.3rem', height: '1.3rem', fill: 'currentColor' }} /></span>
            <h3>My Results</h3>
            <p>View your exam results and scores</p>
          </Link>
          <Link to="/profile" className="action-card">
            <span className="action-icon"><User style={{ width: '1.3rem', height: '1.3rem', fill: 'currentColor' }} /></span>
            <h3>My Profile</h3>
            <p>View and update your profile</p>
          </Link>
          <Link to="/change-password" className="action-card">
            <span className="action-icon"><Key style={{ width: '1.3rem', height: '1.3rem', fill: 'currentColor' }} /></span>
            <h3>Change Password</h3>
            <p>Update your account password</p>
          </Link>
        </div>
      </div>
    </div>
  );
};
