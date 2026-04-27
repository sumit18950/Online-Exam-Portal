import { useContext } from 'react';
import { Link } from 'react-router-dom';
import { AuthContext } from '../../context/AuthContext';
import { BookOpen, BarChart, User, Key } from '../../components/Icons';
import { Card } from '../../components/ui';
import { RoleSidebar } from '../../components/RoleSidebar';
import './Student.css';

export const StudentDashboard = () => {
  const { user } = useContext(AuthContext);

  const cards = [
    { to: '/student/exams', title: 'View Exams', desc: 'Browse and attempt available exams', icon: BookOpen },
    { to: '/student/results', title: 'My Results', desc: 'View your exam results and scores', icon: BarChart },
    { to: '/profile', title: 'My Profile', desc: 'View and update your profile', icon: User },
    { to: '/change-password', title: 'Change Password', desc: 'Update your account password', icon: Key },
  ];

  return (
    <div className="role-layout">
      <RoleSidebar role="STUDENT" />

      <main className="role-main">
        <Card title="Student Dashboard" subtitle={`Welcome, ${user?.username || 'Student'}`}>
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
