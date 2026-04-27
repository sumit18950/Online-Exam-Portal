import { useContext, useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { AuthContext } from '../../context/AuthContext';
import { HiOutlineUsers, HiOutlineUserPlus, HiOutlineBookOpen, HiOutlineClipboardDocumentList, HiOutlineChartBar, HiOutlineUser, HiOutlineSquares2X2 } from 'react-icons/hi2';
import api from '../../services/api';
import './Admin.css';

export const AdminDashboard = () => {
  const { user } = useContext(AuthContext);
  const [stats, setStats] = useState({ totalUsers: 0 });

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const res = await api.get('/api/users/all');
        const users = Array.isArray(res.data) ? res.data : [];
        setStats({ totalUsers: users.length });
      } catch (err) {
        console.error('Failed to fetch stats:', err);
      }
    };
    fetchStats();
  }, []);

  const actions = [
    { to: '/admin/users', icon: <HiOutlineUsers />, title: 'View All Users', desc: 'Manage registered users and their roles' },
    { to: '/admin/create-user', icon: <HiOutlineUserPlus />, title: 'Create User', desc: 'Create a new user with a specific role' },
    { to: '/admin/subjects', icon: <HiOutlineSquares2X2 />, title: 'Manage Subjects', desc: 'Create, edit, and delete subjects' },
    { to: '/admin/exams', icon: <HiOutlineBookOpen />, title: 'Manage Exams', desc: 'Create, edit, and manage all exams', variant: 'danger' },
    { to: '/admin/questions', icon: <HiOutlineClipboardDocumentList />, title: 'Manage Questions', desc: 'View, add, and delete questions' },
    { to: '/admin/results', icon: <HiOutlineChartBar />, title: 'View Results', desc: 'View all student exam results' },
    { to: '/profile', icon: <HiOutlineUser />, title: 'My Profile', desc: 'View and update your profile' },
  ];

  return (
    <div className="container">
      <div className="dashboard-card">
        <h2>Admin Dashboard</h2>
        <p className="welcome-msg">Welcome, {user?.username || 'Admin'}!</p>
        <p className="role-label">Role: ADMIN</p>

        <div className="stats-grid">
          <div className="stat-box">
            <h3>{stats.totalUsers}</h3>
            <p>Total Users</p>
          </div>
        </div>

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
