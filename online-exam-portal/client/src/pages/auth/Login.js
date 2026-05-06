import { useState, useContext } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { GoogleLogin } from '@react-oauth/google';
import api from '../../services/api';
import { storeToken, storeUserData, extractRole } from '../../utils/authUtil';
import { AuthContext } from '../../context/AuthContext';
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

  const completeLogin = async (token) => {
    storeToken(token);
    const profileResponse = await api.get('/api/users/profile');
    const userData = profileResponse.data;
    storeUserData(userData);

    const role = extractRole(userData.role);
    login(token, { id: userData.id, username: userData.username, email: userData.email, role: userData.role });

    switch (role) {
      case 'ADMIN':
        navigate('/admin-dashboard');
        break;
      case 'TEACHER':
        navigate('/teacher-dashboard');
        break;
      default:
        navigate('/student-dashboard');
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const response = await api.post('/api/auth/login', { email, password });
      const token = typeof response.data === 'string'
        ? response.data
        : response.data?.token || response.data?.data?.token;

      if (!token) {
        throw new Error('Token not received from server');
      }

      await completeLogin(token);
    } catch (err) {
      setError(getErrorMessage(err, 'Login failed. Please check your credentials.'));
    } finally {
      setLoading(false);
    }
  };

  const handleGoogleSuccess = async (credentialResponse) => {
    setError('');
    setLoading(true);
    try {
      const response = await api.post('/api/auth/google', {
        credential: credentialResponse.credential,
      });
      const token = typeof response.data === 'string'
        ? response.data
        : response.data?.token;

      if (!token) {
        throw new Error('Token not received from server');
      }

      await completeLogin(token);
    } catch (err) {
      setError(getErrorMessage(err, 'Google login failed. Please try again.'));
    } finally {
      setLoading(false);
    }
  };

  const handleGoogleError = () => {
    setError('Google login was cancelled or failed. Please try again.');
  };

  return (
    <div className="auth-container">
      <div className="auth-card">
        <h2>Login</h2>
        {error && <div className="error-message">{error}</div>}
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="email">Email</label>
            <input
              type="email"
              id="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
              disabled={loading}
            />
          </div>
          <div className="form-group">
            <label htmlFor="password">Password</label>
            <input
              type="password"
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              disabled={loading}
            />
          </div>
          <button type="submit" disabled={loading} className="submit-btn">
            {loading ? 'Logging in...' : 'Login'}
          </button>
        </form>

        <div className="auth-divider">
          <span>OR</span>
        </div>

        <div className="google-btn-wrapper">
          <GoogleLogin
            onSuccess={handleGoogleSuccess}
            onError={handleGoogleError}
            text="signin_with"
            shape="rectangular"
            size="large"
            width="100%"
          />
        </div>

        <p className="form-footer">
          Don't have an account? <Link to="/register">Register here</Link>
        </p>
      </div>
    </div>
  );
};
