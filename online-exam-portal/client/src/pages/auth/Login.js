import { useState, useContext } from 'react';
import { Link, useNavigate } from 'react-router-dom';
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

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      // Step 1: Login and get JWT token
      const response = await api.post('/api/auth/login', { email, password });
      const token = typeof response.data === 'string'
        ? response.data
        : response.data?.token || response.data?.data?.token;

      if (!token) {
        throw new Error('Token not received from server');
      }

      storeToken(token);

      // Step 2: Fetch user profile to get role (JWT only contains email)
      const profileResponse = await api.get('/api/users/profile');
      const userData = profileResponse.data;
      storeUserData(userData);

      const role = extractRole(userData.role);
      login(token, { id: userData.id, username: userData.username, email: userData.email, role: userData.role });

      // Step 3: Redirect to role-based dashboard
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
      setError(getErrorMessage(err, 'Login failed. Please check your credentials.'));
    } finally {
      setLoading(false);
    }
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
        <p className="form-footer">
          Don't have an account? <Link to="/register">Register here</Link>
        </p>
      </div>
    </div>
  );
};
