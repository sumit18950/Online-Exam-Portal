import { useState, useContext } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { GoogleLogin } from '@react-oauth/google';
import api from '../../services/api';
import { storeToken, storeUserData, extractRole } from '../../utils/authUtil';
import { AuthContext } from '../../context/AuthContext';
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
  const { login } = useContext(AuthContext);

  const getErrorMessage = (err, fallback) => {
    const payload = err.response?.data;
    if (typeof payload === 'string') {
      return payload;
    }
    return payload?.message || payload?.error || err.message || fallback;
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (formData.password !== formData.confirmPassword) {
      setError('Passwords do not match');
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
      const message = getErrorMessage(err, 'Registration failed. Please try again.');
      setError(message);
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
    } catch (err) {
      setError(getErrorMessage(err, 'Google sign-up failed. Please try again.'));
    } finally {
      setLoading(false);
    }
  };

  const handleGoogleError = () => {
    setError('Google sign-up was cancelled or failed. Please try again.');
  };

  return (
    <div className="auth-container">
      <div className="auth-card">
        <h2>Register</h2>
        {error && <div className="error-message">{error}</div>}
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="username">Username</label>
            <input
              type="text"
              id="username"
              name="username"
              value={formData.username}
              onChange={handleChange}
              required
              disabled={loading}
            />
          </div>
          <div className="form-group">
            <label htmlFor="email">Email</label>
            <input
              type="email"
              id="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              required
              disabled={loading}
            />
          </div>
          <div className="form-group">
            <label htmlFor="password">Password</label>
            <input
              type="password"
              id="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              required
              disabled={loading}
            />
          </div>
          <div className="form-group">
            <label htmlFor="confirmPassword">Confirm Password</label>
            <input
              type="password"
              id="confirmPassword"
              name="confirmPassword"
              value={formData.confirmPassword}
              onChange={handleChange}
              required
              disabled={loading}
            />
          </div>
          <button type="submit" disabled={loading} className="submit-btn">
            {loading ? 'Registering...' : 'Register'}
          </button>
        </form>

        <div className="auth-divider">
          <span>OR</span>
        </div>

        <div className="google-btn-wrapper">
          <GoogleLogin
            onSuccess={handleGoogleSuccess}
            onError={handleGoogleError}
            text="signup_with"
            shape="rectangular"
            size="large"
            width="100%"
          />
        </div>

        <p className="form-footer">
          Already have an account? <Link to="/login">Login here</Link>
        </p>
      </div>
    </div>
  );
};
