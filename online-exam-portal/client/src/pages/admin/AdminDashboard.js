import { useContext, useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { AuthContext } from '../../context/AuthContext';
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
          <Link to="/admin/users" className="action-card">
            <h3>View All Users</h3>
            <p>Manage registered users and their roles</p>
          </Link>
          <Link to="/admin/create-user" className="action-card">
            <h3>Create User</h3>
            <p>Create a new user with a specific role</p>
          </Link>
          <Link to="/profile" className="action-card">
            <h3>My Profile</h3>
            <p>View and update your profile</p>
          </Link>
        </div>
      </div>
    </div>
  );
};
