import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../services/api';
import './User.css';

const getRoleLabel = (roleValue) => {
  if (!roleValue) return 'N/A';
  const raw = typeof roleValue === 'object' ? roleValue.roleName || '' : String(roleValue);
  const upper = raw.trim().toUpperCase();
  if (!upper) return 'N/A';
  return upper.startsWith('ROLE_') ? upper.substring(5) : upper;
};

export const Profile = () => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    fetchProfile();
  }, []);

  const fetchProfile = async () => {
    try {
      const response = await api.get('/api/users/profile');
      setUser(response.data);
      setError('');
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to fetch profile');
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <div className="container"><div className="loading">Loading...</div></div>;
  if (error) return <div className="container"><div className="error-message">{error}</div></div>;

  return (
    <div className="container">
      <div className="profile-card">
        <h2>My Profile</h2>
        {user && (
          <div className="profile-info">
            <div className="info-group">
              <label>Username:</label>
              <p>{user.username}</p>
            </div>
            <div className="info-group">
              <label>Email:</label>
              <p>{user.email}</p>
            </div>
            <div className="info-group">
              <label>Role:</label>
              <p>{getRoleLabel(user.role)}</p>
            </div>
            <div className="info-group">
              <label>Created At:</label>
              <p>{user.createdAt ? new Date(user.createdAt).toLocaleString() : 'N/A'}</p>
            </div>
          </div>
        )}
        <div className="button-group">
          <button onClick={() => navigate('/update-profile')} className="btn btn-primary">Update Profile</button>
          <button onClick={() => navigate('/change-password')} className="btn btn-secondary">Change Password</button>
        </div>
      </div>
    </div>
  );
};
