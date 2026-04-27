import { useState, useContext } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import api from '../../services/api';
import { storeToken, storeUserData, extractRole } from '../../utils/authUtil';
import { AuthContext } from '../../context/AuthContext';
import { Alert, Button, Card, FormField } from '../../components/ui';
import logo from '../../logo.svg';
import './Auth.css';

export const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const { login } = useContext(AuthContext);

  const getErrorMessage = (err, fallback) => {
    const payload = err.response?.data;
    if (typeof payload === 'string') return payload;
    return payload?.message || payload?.error || err.message || fallback;
  };

  const validateForm = () => {
    if (!email.trim() || !password.trim()) {
      setError('Please enter email and password.');
      return false;
    }
    return true;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    if (!validateForm()) return;

    setLoading(true);
    try {
      // Step 1: Login and get token
      const response = await api.post('/api/auth/login', { email, password });
      const token = typeof response.data === 'string'
        ? response.data
        : response.data?.token || response.data?.data?.token;

      if (!token) {
        throw new Error('Token not received from server');
      }

      storeToken(token);

      // Step 2: Fetch profile (backend keeps role in profile response)
      const profileResponse = await api.get('/api/users/profile');
      const userData = profileResponse.data;
      storeUserData(userData);

      const role = extractRole(userData.role);
      login(token, { id: userData.id, username: userData.username, email: userData.email, role: userData.role });

      // Step 3: Redirect by role
      if (role === 'ADMIN') {
        navigate('/admin-dashboard');
      } else if (role === 'TEACHER') {
        navigate('/teacher-dashboard');
      } else {
        navigate('/student-dashboard');
      }
    } catch (err) {
      setError(getErrorMessage(err, 'Login failed. Please check your credentials.'));
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-layout">
      <section className="auth-aside">
        <img src={logo} alt="Exam portal" />
        <h2>Welcome back</h2>
        <p>Sign in to continue your exam workflow with role-based access and secure sessions.</p>
      </section>

      <Card className="auth-form-card" title="Login" subtitle="Enter your credentials to continue.">
        <Alert type="error">{error}</Alert>
        <form onSubmit={handleSubmit}>
          <FormField id="email" label="Email">
            <input
              type="email"
              id="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
              disabled={loading}
              placeholder="you@example.com"
            />
          </FormField>

          <FormField id="password" label="Password">
            <input
              type="password"
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              disabled={loading}
              placeholder="Enter password"
            />
          </FormField>

          <Button type="submit" fullWidth disabled={loading}>
            {loading ? 'Logging in...' : 'Login'}
          </Button>
        </form>

        <p className="auth-footer-text">
          Do not have an account? <Link to="/register">Create one</Link>
        </p>
      </Card>
    </div>
  );
};
