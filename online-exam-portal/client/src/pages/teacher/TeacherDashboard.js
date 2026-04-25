import { useContext } from 'react';
import { Link } from 'react-router-dom';
import { AuthContext } from '../../context/AuthContext';
import { HiOutlineSquares2X2, HiOutlineBookOpen, HiOutlineClipboardDocumentList, HiOutlineChartBar, HiOutlineUser } from 'react-icons/hi2';
import './Teacher.css';

export const TeacherDashboard = () => {
  const { user } = useContext(AuthContext);

  const actions = [
    { to: '/teacher/subjects', icon: <HiOutlineSquares2X2 />, title: 'Manage Subjects', desc: 'Add, edit, or delete subjects' },
    { to: '/teacher/exams', icon: <HiOutlineBookOpen />, title: 'Manage Exams', desc: 'Create and manage exams' },
    { to: '/teacher/questions', icon: <HiOutlineClipboardDocumentList />, title: 'Manage Questions', desc: 'Create, update, and delete questions' },
    { to: '/teacher/results', icon: <HiOutlineChartBar />, title: 'View Results', desc: 'View student exam results' },
    { to: '/profile', icon: <HiOutlineUser />, title: 'My Profile', desc: 'View and update your profile' },
  ];

  return (
    <div className="container">
      <div className="dashboard-card">
        <h2>Teacher Dashboard</h2>
        <p className="welcome-msg">Welcome, {user?.username || 'Teacher'}!</p>
        <p className="role-label">Role: TEACHER</p>

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
