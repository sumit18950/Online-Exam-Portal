import { useContext } from 'react';
import { Link } from 'react-router-dom';
import { AuthContext } from '../../context/AuthContext';
import { Layers, BookOpen, FileText, BarChart, User } from '../../components/Icons';
import './Teacher.css';

export const TeacherDashboard = () => {
  const { user } = useContext(AuthContext);

  return (
    <div className="container">
      <div className="dashboard-card">
        <h2>Teacher Dashboard</h2>
        <p className="welcome-msg">Welcome, {user?.username || 'Teacher'}!</p>
        <p className="role-label">Role: TEACHER</p>

        <div className="dashboard-actions">
          <Link to="/teacher/subjects" className="action-card">
            <span className="action-icon"><Layers style={{ width: '1.3rem', height: '1.3rem', fill: 'currentColor' }} /></span>
            <h3>Manage Subjects</h3>
            <p>Add, edit, or delete subjects</p>
          </Link>
          <Link to="/teacher/exams" className="action-card">
            <span className="action-icon"><BookOpen style={{ width: '1.3rem', height: '1.3rem', fill: 'currentColor' }} /></span>
            <h3>Manage Exams</h3>
            <p>Create and manage exams</p>
          </Link>
          <Link to="/teacher/questions" className="action-card">
            <span className="action-icon"><FileText style={{ width: '1.3rem', height: '1.3rem', fill: 'currentColor' }} /></span>
            <h3>Manage Questions</h3>
            <p>Create, update, and delete questions</p>
          </Link>
          <Link to="/teacher/results" className="action-card">
            <span className="action-icon"><BarChart style={{ width: '1.3rem', height: '1.3rem', fill: 'currentColor' }} /></span>
            <h3>View Results</h3>
            <p>View student exam results</p>
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
