import { useContext, useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { AuthContext } from '../../context/AuthContext';
import { Users, UserPlus, Layers, BookOpen, FileText, BarChart, User } from '../../components/Icons';
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
            <Users style={{ width: '1.5rem', height: '1.5rem', fill: 'rgba(255,255,255,0.8)', marginBottom: '0.3rem' }} />
            <h3>{stats.totalUsers}</h3>
            <p>Total Users</p>
          </div>
        </div>

        <div className="dashboard-actions">
          <Link to="/admin/users" className="action-card">
            <span className="action-icon"><Users style={{ width: '1.3rem', height: '1.3rem', fill: 'currentColor' }} /></span>
            <h3>View All Users</h3>
            <p>Manage registered users and their roles</p>
          </Link>
          <Link to="/admin/create-user" className="action-card">
            <span className="action-icon"><UserPlus style={{ width: '1.3rem', height: '1.3rem', fill: 'currentColor' }} /></span>
            <h3>Create User</h3>
            <p>Create a new user with a specific role</p>
          </Link>
          <Link to="/admin/subjects" className="action-card">
            <span className="action-icon"><Layers style={{ width: '1.3rem', height: '1.3rem', fill: 'currentColor' }} /></span>
            <h3>Manage Subjects</h3>
            <p>Create, edit, and delete subjects</p>
          </Link>
          <Link to="/admin/exams" className="action-card">
            <span className="action-icon"><BookOpen style={{ width: '1.3rem', height: '1.3rem', fill: 'currentColor' }} /></span>
            <h3>Manage Exams</h3>
            <p>Create, edit, and manage all exams</p>
          </Link>
          <Link to="/admin/questions" className="action-card">
            <span className="action-icon"><FileText style={{ width: '1.3rem', height: '1.3rem', fill: 'currentColor' }} /></span>
            <h3>Manage Questions</h3>
            <p>View, add, and delete questions</p>
          </Link>
          <Link to="/admin/results" className="action-card">
            <span className="action-icon"><BarChart style={{ width: '1.3rem', height: '1.3rem', fill: 'currentColor' }} /></span>
            <h3>View Results</h3>
            <p>View all student exam results</p>
          </Link>
          <Link to="/profile" className="action-card">
            <span className="action-icon"><User style={{ width: '1.3rem', height: '1.3rem', fill: 'currentColor' }} /></span>
            <h3>My Profile</h3>
            <p>View and update your profile</p>
          </Link>
        </div>
      </div>
    </div>
  );
};
