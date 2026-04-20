import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../services/api';
import './Admin.css';

const getRoleLabel = (roleValue) => {
  if (!roleValue) return 'N/A';
  const raw = typeof roleValue === 'object' ? roleValue.roleName || '' : String(roleValue);
  const upper = raw.trim().toUpperCase();
  if (!upper) return 'N/A';
  return upper.startsWith('ROLE_') ? upper.substring(5) : upper;
};

export const ViewAllUsers = () => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    try {
      const response = await api.get('/api/users/all');
      const data = Array.isArray(response.data) ? response.data : (response.data?.data || []);
      setUsers(data);
      setError('');
    } catch (err) {
      const message = err.response?.data?.message || 'Failed to fetch users';
      setError(message);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (userId) => {
    if (window.confirm('Are you sure you want to delete this user?')) {
      try {
        await api.delete(`/api/users/${userId}`);
        setUsers(users.filter((u) => u.id !== userId));
      } catch (err) {
        const message = err.response?.data?.message || 'Failed to delete user';
        setError(message);
      }
    }
  };

  if (loading) return <div className="container"><div className="loading">Loading...</div></div>;

  return (
    <div className="container">
      <div className="admin-card">
        <div className="card-header">
          <h2>All Users</h2>
          <button onClick={() => navigate('/admin/create-user')} className="btn btn-primary">Create User</button>
        </div>
        {error && <div className="error-message">{error}</div>}
        {users.length === 0 ? (
          <p className="no-data">No users found</p>
        ) : (
          <div className="table-responsive">
            <table className="users-table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Username</th>
                  <th>Email</th>
                  <th>Role</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {users.map((user) => {
                  const roleLabel = getRoleLabel(user.role);
                  return (
                    <tr key={user.id}>
                      <td>{user.id}</td>
                      <td>{user.username}</td>
                      <td>{user.email}</td>
                      <td>
                        <span className={`role-badge role-${roleLabel.toLowerCase()}`}>{roleLabel}</span>
                      </td>
                      <td>
                        <button onClick={() => handleDelete(user.id)} className="btn btn-danger btn-small">Delete</button>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
};
