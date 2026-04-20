import React from 'react';
import './Pages.css';

export const Home = () => {
  return (
    <div className="container">
      <div className="home-card">
        <h1>Welcome to Online Exam Portal</h1>
        <p>A comprehensive platform for online examinations and assessments</p>
        <div className="features">
          <h2>Features</h2>
          <ul>
            <li>Secure login with JWT authentication</li>
            <li>Role-based access control (Admin, Teacher, Student)</li>
            <li>Create and manage exams and questions</li>
            <li>Real-time exam attempts with instant feedback</li>
            <li>Comprehensive result analysis</li>
            <li>User profile management</li>
          </ul>
        </div>
      </div>
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
