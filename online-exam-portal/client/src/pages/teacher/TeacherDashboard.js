import { useContext } from 'react';
import { Link } from 'react-router-dom';
import { AuthContext } from '../../context/AuthContext';
import { Layers, BookOpen, FileText, BarChart, User } from '../../components/Icons';
import { Card } from '../../components/ui';
import { RoleSidebar } from '../../components/RoleSidebar';
import './Teacher.css';

export const TeacherDashboard = () => {
  const { user } = useContext(AuthContext);

  const cards = [
    { to: '/teacher/subjects', title: 'Manage Subjects', desc: 'Add, edit, or delete subjects', icon: Layers },
    { to: '/teacher/exams', title: 'Manage Exams', desc: 'Create and manage exams', icon: BookOpen },
    { to: '/teacher/questions', title: 'Manage Questions', desc: 'Create, update, and delete questions', icon: FileText },
    { to: '/teacher/results', title: 'View Results', desc: 'View student exam results', icon: BarChart },
    { to: '/profile', title: 'My Profile', desc: 'View and update your profile', icon: User },
  ];

  return (
    <div className="role-layout">
      <RoleSidebar role="TEACHER" />

      <main className="role-main">
        <Card title="Teacher Dashboard" subtitle={`Welcome, ${user?.username || 'Teacher'}`}>
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
