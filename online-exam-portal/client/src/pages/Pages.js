import { useContext } from 'react';
import { Link } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';
import { Award, BookOpen, Shield, Users } from '../components/Icons';
import logo from '../logo.svg';
import './Pages.css';

export const Home = () => {
  const { isAuthenticated, role } = useContext(AuthContext);

  const getDashboardLink = () => {
    switch (role) {
      case 'ADMIN':
        return '/admin-dashboard';
      case 'TEACHER':
        return '/teacher-dashboard';
      case 'STUDENT':
        return '/student-dashboard';
      default:
        return '/login';
    }
  };

  const features = [
    { title: 'Secure by Default', text: 'JWT auth, role-based access, and protected exam flow.', icon: Shield },
    { title: 'Fast Exam Creation', text: 'Create subjects, exams, and questions in minutes.', icon: BookOpen },
    { title: 'Smart Results', text: 'Get scores, grades, and leaderboards with clear insights.', icon: Award },
    { title: 'Multi-Role Support', text: 'Admin, Teacher, and Student workspaces in one portal.', icon: Users },
  ];

  return (
    <div className="home-page">
      <section className="home-hero">
        <div>
          <p className="home-badge">Online Exam Management Platform</p>
          <h1>Run assessments with a modern, reliable exam portal</h1>
          <p className="home-copy">
            Manage exams end-to-end: onboarding, exam setup, timed attempts, and result analytics.
            Designed for schools, institutes, and training teams.
          </p>
          <div className="home-actions">
            {isAuthenticated ? (
              <Link to={getDashboardLink()} className="ui-btn ui-btn-primary">Go to Dashboard</Link>
            ) : (
              <>
                <Link to="/login" className="ui-btn ui-btn-primary">Get Started</Link>
                <Link to="/register" className="ui-btn ui-btn-ghost">Create Account</Link>
              </>
            )}
          </div>

          <div className="home-stats">
            <div><strong>99.9%</strong><span>Uptime</span></div>
            <div><strong>3 Roles</strong><span>Admin / Teacher / Student</span></div>
            <div><strong>Real-time</strong><span>Timer + Auto Submit</span></div>
          </div>
        </div>

        <div className="hero-art" aria-hidden="true">
          <img src={logo} alt="Online exam portal illustration" />
        </div>
      </section>

      <section className="home-section">
        <h2>Everything you need in one place</h2>
        <p className="section-subtitle">Simple UI for daily use, strong enough for real exam workflows.</p>
        <div className="feature-grid">
          {features.map((item) => {
            const Icon = item.icon;
            return (
              <article key={item.title} className="feature-card">
                <span className="feature-icon"><Icon /></span>
                <h3>{item.title}</h3>
                <p>{item.text}</p>
              </article>
            );
          })}
        </div>
      </section>

      {!isAuthenticated && (
        <section className="home-cta">
          <h2>Ready to modernize your exam process?</h2>
          <p>Start now and move from manual coordination to a structured online workflow.</p>
          <div className="home-actions">
            <Link to="/register" className="ui-btn ui-btn-primary">Create Free Account</Link>
            <Link to="/login" className="ui-btn ui-btn-secondary">Sign In</Link>
          </div>
        </section>
      )}

      <footer className="home-footer">
        <p>Exam Portal - secure assessments, clear outcomes.</p>
      </footer>
    </div>
  );
};

export const Unauthorized = () => {
  return (
    <div className="simple-page-wrap">
      <div className="simple-page-card">
        <h1>Access Denied</h1>
        <p>You do not have permission to view this page.</p>
        <Link to="/" className="ui-btn ui-btn-primary">Go Home</Link>
      </div>
    </div>
  );
};

export const NotFound = () => {
  return (
    <div className="simple-page-wrap">
      <div className="simple-page-card">
        <h1>404 - Page Not Found</h1>
        <p>The page you requested does not exist.</p>
        <Link to="/" className="ui-btn ui-btn-primary">Go Home</Link>
      </div>
    </div>
  );
};
