import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../services/api';
import './Admin.css';

export const CreateUser = () => {
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: '',
    role: 'STUDENT',
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    setLoading(true);

    try {
      await api.post('/api/admin/create-user', formData);
      setSuccess('User created successfully!');
      setFormData({ username: '', email: '', password: '', role: 'STUDENT' });
      setTimeout(() => navigate('/admin/users'), 2000);
    } catch (err) {
      const payload = err.response?.data;
      const message = typeof payload === 'string' ? payload : payload?.message || 'Failed to create user';
      setError(message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container">
      <div className="form-card">
        <h2>Create User</h2>
        {error && <div className="error-message">{error}</div>}
        {success && <div className="success-message">{success}</div>}
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="username">Username</label>
            <input type="text" id="username" name="username" value={formData.username} onChange={handleChange} required disabled={loading} />
          </div>
          <div className="form-group">
            <label htmlFor="email">Email</label>
            <input type="email" id="email" name="email" value={formData.email} onChange={handleChange} required disabled={loading} />
          </div>
          <div className="form-group">
            <label htmlFor="password">Password</label>
            <input type="password" id="password" name="password" value={formData.password} onChange={handleChange} required disabled={loading} />
          </div>
          <div className="form-group">
            <label htmlFor="role">Role</label>
            <select id="role" name="role" value={formData.role} onChange={handleChange} disabled={loading}>
              <option value="STUDENT">Student</option>
              <option value="TEACHER">Teacher</option>
              <option value="ADMIN">Admin</option>
            </select>
          </div>
          <div className="button-group">
            <button type="submit" disabled={loading} className="btn btn-primary">
              {loading ? 'Creating...' : 'Create User'}
            </button>
            <button type="button" onClick={() => navigate('/admin/users')} className="btn btn-secondary">Cancel</button>
          </div>
        </form>
      </div>
    </div>
  );
};
