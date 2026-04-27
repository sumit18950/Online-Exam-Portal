import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import api from '../../services/api';
import { Alert, Button, Card, FormField } from '../../components/ui';
import logo from '../../logo.svg';
import './Auth.css';

export const Register = () => {
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: '',
    confirmPassword: '',
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const getErrorMessage = (err, fallback) => {
    const payload = err.response?.data;
    if (typeof payload === 'string') return payload;
    return payload?.message || payload?.error || err.message || fallback;
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (formData.password !== formData.confirmPassword) {
      setError('Passwords do not match.');
      return;
    }

    setLoading(true);
    try {
      await api.post('/api/auth/register', {
        username: formData.username,
        email: formData.email,
        password: formData.password,
      });
      navigate('/login');
    } catch (err) {
      setError(getErrorMessage(err, 'Registration failed. Please try again.'));
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-layout">
      <section className="auth-aside">
        <img src={logo} alt="Exam portal" />
        <h2>Create your account</h2>
        <p>Join the portal and get a workspace tailored for your role and exam tasks.</p>
      </section>

      <Card className="auth-form-card" title="Register" subtitle="Set up your account in less than a minute.">
        <Alert type="error">{error}</Alert>
        <form onSubmit={handleSubmit}>
          <FormField id="username" label="Username">
            <input
              type="text"
              id="username"
              name="username"
              value={formData.username}
              onChange={handleChange}
              required
              disabled={loading}
              placeholder="Enter username"
            />
          </FormField>

          <FormField id="email" label="Email">
            <input
              type="email"
              id="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              required
              disabled={loading}
              placeholder="you@example.com"
            />
          </FormField>

          <FormField id="password" label="Password">
            <input
              type="password"
              id="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              required
              disabled={loading}
              placeholder="Create password"
            />
          </FormField>

          <FormField id="confirmPassword" label="Confirm Password">
            <input
              type="password"
              id="confirmPassword"
              name="confirmPassword"
              value={formData.confirmPassword}
              onChange={handleChange}
              required
              disabled={loading}
              placeholder="Confirm password"
            />
          </FormField>

          <Button type="submit" fullWidth disabled={loading}>
            {loading ? 'Registering...' : 'Register'}
          </Button>
        </form>

        <p className="auth-footer-text">
          Already have an account? <Link to="/login">Login</Link>
        </p>
      </Card>
    </div>
  );
};
