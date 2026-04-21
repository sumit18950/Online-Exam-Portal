import { useContext } from 'react';
import { Link } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';
import './Pages.css';

export const Home = () => {
  const { isAuthenticated, role } = useContext(AuthContext);

  const getDashboardLink = () => {
    switch (role) {
      case 'ADMIN': return '/admin-dashboard';
      case 'TEACHER': return '/teacher-dashboard';
      case 'STUDENT': return '/student-dashboard';
      default: return '/login';
    }
  };

  return (
    <div className="home-page">
      {/* Hero Section */}
      <section className="hero-section">
        <div className="hero-content">
          <span className="hero-badge">Trusted Online Assessment Platform</span>
          <h1>Conduct Exams with<br />Confidence & Ease</h1>
          <p className="hero-subtitle">
            A secure, reliable platform for creating, managing, and taking online examinations.
            Built for institutions, teachers, and students.
          </p>
          <div className="hero-actions">
            {isAuthenticated ? (
              <Link to={getDashboardLink()} className="btn-hero btn-hero-primary">Go to Dashboard</Link>
            ) : (
              <>
                <Link to="/login" className="btn-hero btn-hero-primary">Get Started</Link>
                <Link to="/register" className="btn-hero btn-hero-outline">Create Account</Link>
              </>
            )}
          </div>
        </div>
        <div className="hero-visual">
          <div className="hero-stat-grid">
            <div className="hero-stat-card">
              <div className="hero-stat-number">100%</div>
              <div className="hero-stat-label">Secure</div>
            </div>
            <div className="hero-stat-card accent">
              <div className="hero-stat-number">24/7</div>
              <div className="hero-stat-label">Available</div>
            </div>
            <div className="hero-stat-card accent">
              <div className="hero-stat-number">Live</div>
              <div className="hero-stat-label">Timer</div>
            </div>
            <div className="hero-stat-card">
              <div className="hero-stat-number">Auto</div>
              <div className="hero-stat-label">Grading</div>
            </div>
          </div>
        </div>
      </section>

      {/* How It Works */}
      <section className="how-it-works">
        <h2>How It Works</h2>
        <div className="steps-grid">
          <div className="step-card">
            <div className="step-number">1</div>
            <h3>Create an Account</h3>
            <p>Register as an admin, teacher, or student to access your personalized dashboard.</p>
          </div>
          <div className="step-card">
            <div className="step-number">2</div>
            <h3>Set Up Exams</h3>
            <p>Teachers create subjects, schedule exams, and add questions with multiple options.</p>
          </div>
          <div className="step-card">
            <div className="step-number">3</div>
            <h3>Take the Exam</h3>
            <p>Students attempt exams within the scheduled time with a live countdown timer.</p>
          </div>
          <div className="step-card">
            <div className="step-number">4</div>
            <h3>View Results</h3>
            <p>Get instant results with scores, grades, and detailed performance analysis.</p>
          </div>
        </div>
      </section>

      {/* Role Highlights */}
      <section className="roles-section">
        <h2>Built for Everyone</h2>
        <div className="roles-grid">
          <div className="role-card role-admin-card">
            <div className="role-icon">A</div>
            <h3>Administrators</h3>
            <ul>
              <li>Manage all users and roles</li>
              <li>Full control over subjects and exams</li>
              <li>View all results and analytics</li>
              <li>Create and edit questions</li>
            </ul>
          </div>
          <div className="role-card role-teacher-card">
            <div className="role-icon">T</div>
            <h3>Teachers</h3>
            <ul>
              <li>Create and manage subjects</li>
              <li>Schedule exams with custom duration</li>
              <li>Add, edit, and organize questions</li>
              <li>Review student performance</li>
            </ul>
          </div>
          <div className="role-card role-student-card">
            <div className="role-icon">S</div>
            <h3>Students</h3>
            <ul>
              <li>Browse and enroll in exams</li>
              <li>Timed exam attempts with auto-submit</li>
              <li>Real-time answer saving</li>
              <li>Instant results and grade reports</li>
            </ul>
          </div>
        </div>
      </section>

      {/* CTA */}
      {!isAuthenticated && (
        <section className="cta-section">
          <h2>Ready to Get Started?</h2>
          <p>Join the platform and start your assessment journey today.</p>
          <div className="hero-actions">
            <Link to="/register" className="btn-hero btn-hero-primary">Create Free Account</Link>
            <Link to="/login" className="btn-hero btn-hero-outline">Sign In</Link>
          </div>
        </section>
      )}

      {/* Footer */}
      <footer className="home-footer">
        <p>Online Exam Portal &mdash; Secure assessments, simplified.</p>
      </footer>
    </div>
  );
};

export const Unauthorized = () => {
  return (
    <div className="container">
      <div className="error-card">
        <h1>Access Denied</h1>
        <p>You do not have permission to access this page.</p>
        <a href="/" className="btn btn-primary">Go to Home</a>
      </div>
    </div>
  );
};

export const NotFound = () => {
  return (
    <div className="container">
      <div className="error-card">
        <h1>404 - Page Not Found</h1>
        <p>The page you are looking for does not exist.</p>
        <a href="/" className="btn btn-primary">Go to Home</a>
      </div>
    </div>
  );
};
