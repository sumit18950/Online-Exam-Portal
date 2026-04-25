import { useContext } from 'react';
import { Link } from 'react-router-dom';
import { AuthContext } from '../../context/AuthContext';
import { HiOutlineBookOpen, HiOutlineChartBar, HiOutlineUser, HiOutlineKey } from 'react-icons/hi2';
import './Student.css';

export const StudentDashboard = () => {
  const { user } = useContext(AuthContext);

  const actions = [
    { to: '/student/exams', icon: <HiOutlineBookOpen />, title: 'View Exams', desc: 'Browse and attempt available exams' },
    { to: '/student/results', icon: <HiOutlineChartBar />, title: 'My Results', desc: 'View your exam results and scores' },
    { to: '/profile', icon: <HiOutlineUser />, title: 'My Profile', desc: 'View and update your profile' },
    { to: '/change-password', icon: <HiOutlineKey />, title: 'Change Password', desc: 'Update your account password' },
  ];

  return (
    <div className="container">
      <div className="dashboard-card">
        <h2>Student Dashboard</h2>
        <p className="welcome-msg">Welcome, {user?.username || 'Student'}!</p>
        <p className="role-label">Role: STUDENT</p>

        <div className="dashboard-actions">
          {actions.map((a) => (
            <Link key={a.to} to={a.to} className="action-card">
              <span className="action-icon">{a.icon}</span>
              <h3>{a.title}</h3>
              <p>{a.desc}</p>
            </Link>
          ))}
        </div>
      </div>
    </div>
  );
};
