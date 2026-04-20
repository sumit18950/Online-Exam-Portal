import { useEffect, useState } from 'react';
import api from '../../services/api';
import './Admin.css';

export const AdminManageSubjects = () => {
  const [subjects, setSubjects] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [showForm, setShowForm] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [formData, setFormData] = useState({ subject: '', description: '' });
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    fetchSubjects();
  }, []);

  const fetchSubjects = async () => {
    try {
      const response = await api.get('/api/exams/subjects');
      const data = Array.isArray(response.data) ? response.data : (response.data?.data || []);
      setSubjects(data);
      setError('');
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to fetch subjects');
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const resetForm = () => {
    setFormData({ subject: '', description: '' });
    setEditingId(null);
    setShowForm(false);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    setSubmitting(true);

    try {
      if (editingId) {
        const res = await api.put(`/api/exams/subjects/${editingId}`, formData);
        setSubjects(subjects.map((s) => (s.id === editingId ? res.data : s)));
        setSuccess('Subject updated successfully!');
      } else {
        const res = await api.post('/api/exams/subjects', formData);
        setSubjects([...subjects, res.data]);
        setSuccess('Subject created successfully!');
      }
      resetForm();
    } catch (err) {
      const payload = err.response?.data;
      setError(typeof payload === 'string' ? payload : payload?.message || 'Failed to save subject');
    } finally {
      setSubmitting(false);
    }
  };

  const handleEdit = (subject) => {
    setFormData({ subject: subject.subjectName || '', description: subject.description || '' });
    setEditingId(subject.id);
    setShowForm(true);
    setError('');
    setSuccess('');
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this subject?')) {
      try {
        await api.delete(`/api/exams/subjects/${id}`);
        setSubjects(subjects.filter((s) => s.id !== id));
        setSuccess('Subject deleted successfully!');
      } catch (err) {
        setError(err.response?.data?.message || 'Failed to delete subject');
      }
    }
  };

  if (loading) return <div className="container"><div className="loading">Loading...</div></div>;

  return (
    <div className="container">
      <div className="admin-card">
        <div className="card-header">
          <h2>Manage Subjects</h2>
          <button onClick={() => { resetForm(); setShowForm(!showForm); }} className="btn btn-primary">
            {showForm ? 'Cancel' : 'Add Subject'}
          </button>
        </div>

        {error && <div className="error-message">{error}</div>}
        {success && <div className="success-message">{success}</div>}

        {showForm && (
          <form onSubmit={handleSubmit} className="inline-form">
            <div className="form-group">
              <label htmlFor="subject">Subject Name</label>
              <input type="text" id="subject" name="subject" value={formData.subject} onChange={handleChange} required disabled={submitting} />
            </div>
            <div className="form-group">
              <label htmlFor="description">Description</label>
              <textarea id="description" name="description" value={formData.description} onChange={handleChange} disabled={submitting} rows="3" />
            </div>
            <div className="button-group">
              <button type="submit" disabled={submitting} className="btn btn-primary">
                {submitting ? 'Saving...' : (editingId ? 'Update Subject' : 'Create Subject')}
              </button>
              <button type="button" onClick={resetForm} className="btn btn-secondary">Cancel</button>
            </div>
          </form>
        )}

        {subjects.length === 0 ? (
          <p className="no-data">No subjects found. Add one to get started!</p>
        ) : (
          <div className="table-responsive">
            <table className="users-table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Subject Name</th>
                  <th>Description</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {subjects.map((subject) => (
                  <tr key={subject.id}>
                    <td>{subject.id}</td>
                    <td>{subject.subjectName}</td>
                    <td>{subject.description || '-'}</td>
                    <td>
                      <div className="action-buttons">
                        <button onClick={() => handleEdit(subject)} className="btn btn-secondary btn-small">Edit</button>
                        <button onClick={() => handleDelete(subject.id)} className="btn btn-danger btn-small">Delete</button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
};
