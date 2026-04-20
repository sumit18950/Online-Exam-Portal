import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../services/api';
import './User.css';

export const UpdateProfile = () => {
  const [username, setUsername] = useState('');
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    fetchProfile();
  }, []);

  const fetchProfile = async () => {
    try {
      const response = await api.get('/api/users/profile');
      const user = response.data;
      setUsername(user.username || '');
      setError('');
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to fetch profile');
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    setError('');
    setSuccess('');

    try {
      await api.put('/api/users/update-profile', { username });
      setSuccess('Profile updated successfully!');
      localStorage.setItem('user_name', username);
      setTimeout(() => navigate('/profile'), 2000);
    } catch (err) {
      const payload = err.response?.data;
      setError(typeof payload === 'string' ? payload : payload?.message || 'Failed to update profile');
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) return <div className="container"><div className="loading">Loading...</div></div>;

  return (
    <div className="container">
      <div className="form-card">
        <h2>Update Profile</h2>
        {error && <div className="error-message">{error}</div>}
        {success && <div className="success-message">{success}</div>}
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="username">Username</label>
            <input
              type="text"
              id="username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
              disabled={submitting}
            />
          </div>
          <div className="button-group">
            <button type="submit" disabled={submitting} className="btn btn-primary">
              {submitting ? 'Updating...' : 'Update'}
            </button>
            <button type="button" onClick={() => navigate('/profile')} className="btn btn-secondary">Cancel</button>
          </div>
        </form>
      </div>
    </div>
  );
};
