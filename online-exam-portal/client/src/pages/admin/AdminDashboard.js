import { useContext, useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { AuthContext } from '../../context/AuthContext';
import { Users, UserPlus, Layers, BookOpen, FileText, BarChart, User } from '../../components/Icons';
import { Card, Loader } from '../../components/ui';
import { RoleSidebar } from '../../components/RoleSidebar';
import api from '../../services/api';
import './Admin.css';

export const AdminDashboard = () => {
  const { user } = useContext(AuthContext);
  const [stats, setStats] = useState({ totalUsers: 0 });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const res = await api.get('/api/users/all');
        const users = Array.isArray(res.data) ? res.data : [];
        setStats({ totalUsers: users.length });
      } catch (err) {
        console.error('Failed to fetch stats:', err);
      } finally {
        setLoading(false);
      }
    };
    fetchStats();
  }, []);

  const cards = [
    { to: '/admin/users', title: 'View All Users', desc: 'Manage registered users and their roles', icon: Users },
    { to: '/admin/create-user', title: 'Create User', desc: 'Add a new user with a specific role', icon: UserPlus },
    { to: '/admin/subjects', title: 'Manage Subjects', desc: 'Create and organize all subjects', icon: Layers },
    { to: '/admin/exams', title: 'Manage Exams', desc: 'Create, edit, and publish exams', icon: BookOpen },
    { to: '/admin/questions', title: 'Manage Questions', desc: 'Maintain question bank and quality', icon: FileText },
    { to: '/admin/results', title: 'View Results', desc: 'Inspect exam outcomes and grading', icon: BarChart },
    { to: '/profile', title: 'My Profile', desc: 'Review and update personal details', icon: User },
  ];

  return (
    <div className="role-layout">
      <RoleSidebar role="ADMIN" />

      <main className="role-main">
        <Card title="Admin Dashboard" subtitle={`Welcome, ${user?.username || 'Admin'}`}>
          {loading ? (
            <Loader text="Loading dashboard stats..." />
          ) : (
            <div className="stats-grid">
              <div className="stat-box">
                <Users style={{ width: '1.5rem', height: '1.5rem', fill: 'rgba(255,255,255,0.9)', marginBottom: '0.3rem' }} />
                <h3>{stats.totalUsers}</h3>
                <p>Total Users</p>
              </div>
            </div>
          )}

          <div className="dashboard-actions">
            {cards.map((card) => {
              const Icon = card.icon;
              return (
                <Link to={card.to} className="action-card" key={card.to}>
                  <span className="action-icon"><Icon style={{ width: '1.2rem', height: '1.2rem', fill: 'currentColor' }} /></span>
                  <h3>{card.title}</h3>
                  <p>{card.desc}</p>
                </Link>
              );
            })}
          </div>
        </Card>
      </main>
    </div>
  );
};
