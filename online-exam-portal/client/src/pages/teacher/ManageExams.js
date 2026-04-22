import { useEffect, useState, useContext } from 'react';
import { AuthContext } from '../../context/AuthContext';
import api from '../../services/api';
import './Teacher.css';

export const ManageExams = () => {
  const { user } = useContext(AuthContext);
  const [exams, setExams] = useState([]);
  const [subjects, setSubjects] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [showForm, setShowForm] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [submitting, setSubmitting] = useState(false);
  const [formData, setFormData] = useState({
    examTitle: '',
    examDate: '',
    examTime: '',
    durationMinutes: 60,
    subjectId: '',
    examType: 'MULTIPLE_CHOICE',
    status: 'SCHEDULED',
  });

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const [examsRes, subjectsRes] = await Promise.all([
        api.get('/api/exams/teacher/all'),
        api.get('/api/exams/subjects'),
      ]);
      setExams(Array.isArray(examsRes.data) ? examsRes.data : []);
      setSubjects(Array.isArray(subjectsRes.data) ? subjectsRes.data : []);
      setError('');
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to fetch data');
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const resetForm = () => {
    setFormData({ examTitle: '', examDate: '', examTime: '', durationMinutes: 60, subjectId: '', examType: 'MULTIPLE_CHOICE', status: 'SCHEDULED' });
    setEditingId(null);
    setShowForm(false);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    setSubmitting(true);

    const payload = {
      ...formData,
      subjectId: parseInt(formData.subjectId),
      durationMinutes: parseInt(formData.durationMinutes),
      createdBy: parseInt(user?.id),
    };

    try {
      if (editingId) {
        const res = await api.put(`/api/exams/teacher/update/${editingId}`, payload);
        setExams(exams.map((ex) => (ex.id === editingId ? res.data : ex)));
        setSuccess('Exam updated successfully!');
      } else {
        const res = await api.post('/api/exams/teacher/create', payload);
        setExams([...exams, res.data]);
        setSuccess('Exam created successfully!');
      }
      resetForm();
    } catch (err) {
      const payload2 = err.response?.data;
      setError(typeof payload2 === 'string' ? payload2 : payload2?.message || 'Failed to save exam');
    } finally {
      setSubmitting(false);
    }
  };

  const handleEdit = (exam) => {
    setFormData({
      examTitle: exam.examTitle || '',
      examDate: exam.examDate || '',
      examTime: exam.examTime || '',
      durationMinutes: exam.durationMinutes || 60,
      subjectId: exam.subjectId || '',
      examType: exam.examType || 'MULTIPLE_CHOICE',
      status: exam.status || 'SCHEDULED',
    });
    setEditingId(exam.id);
    setShowForm(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this exam?')) {
      try {
        await api.delete(`/api/exams/teacher/delete/${id}`);
        setExams(exams.filter((ex) => ex.id !== id));
        setSuccess('Exam deleted!');
      } catch (err) {
        setError(err.response?.data?.message || 'Failed to delete exam');
      }
    }
  };

  if (loading) return <div className="container"><div className="loading">Loading...</div></div>;

  return (
    <div className="container">
      <div className="teacher-card">
        <div className="card-header">
          <h2>Manage Exams</h2>
          <button onClick={() => { resetForm(); setShowForm(!showForm); }} className="btn btn-primary">
            {showForm ? 'Cancel' : 'Create Exam'}
          </button>
        </div>

        {error && <div className="error-message">{error}</div>}
        {success && <div className="success-message">{success}</div>}

        {showForm && (
          <form onSubmit={handleSubmit} className="inline-form">
            <div className="form-row">
              <div className="form-group">
                <label htmlFor="examTitle">Exam Title</label>
                <input type="text" id="examTitle" name="examTitle" value={formData.examTitle} onChange={handleChange} required disabled={submitting} />
              </div>
              <div className="form-group">
                <label htmlFor="subjectId">Subject</label>
                <select id="subjectId" name="subjectId" value={formData.subjectId} onChange={handleChange} required disabled={submitting}>
                  <option value="">Select Subject</option>
                  {subjects.map((s) => <option key={s.id} value={s.id}>{s.subjectName}</option>)}
                </select>
              </div>
            </div>
            <div className="form-row">
              <div className="form-group">
                <label htmlFor="examDate">Date</label>
                <input type="date" id="examDate" name="examDate" value={formData.examDate} onChange={handleChange} required disabled={submitting} />
              </div>
              <div className="form-group">
                <label htmlFor="examTime">Time</label>
                <input type="time" id="examTime" name="examTime" value={formData.examTime} onChange={handleChange} required disabled={submitting} step="1" />
              </div>
              <div className="form-group">
                <label htmlFor="durationMinutes">Duration (min)</label>
                <input type="number" id="durationMinutes" name="durationMinutes" value={formData.durationMinutes} onChange={handleChange} required min="1" disabled={submitting} />
              </div>
            </div>
            <div className="form-row">
              <div className="form-group">
                <label htmlFor="examType">Type</label>
                <select id="examType" name="examType" value={formData.examType} onChange={handleChange} disabled={submitting}>
                  <option value="MULTIPLE_CHOICE">Multiple Choice</option>
                </select>
              </div>
              <div className="form-group">
                <label htmlFor="status">Status</label>
                <select id="status" name="status" value={formData.status} onChange={handleChange} disabled={submitting}>
                  <option value="SCHEDULED">Scheduled</option>
                  <option value="ONGOING">Ongoing</option>
                  <option value="COMPLETED">Completed</option>
                </select>
              </div>
            </div>
            <div className="button-group">
              <button type="submit" disabled={submitting} className="btn btn-primary">
                {submitting ? 'Saving...' : (editingId ? 'Update Exam' : 'Create Exam')}
              </button>
              <button type="button" onClick={resetForm} className="btn btn-secondary">Cancel</button>
            </div>
          </form>
        )}

        {exams.length === 0 ? (
          <p className="no-data">No exams found.</p>
        ) : (
          <div className="table-responsive">
            <table className="users-table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Title</th>
                  <th>Subject</th>
                  <th>Date</th>
                  <th>Duration</th>
                  <th>Type</th>
                  <th>Status</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {exams.map((exam) => (
                  <tr key={exam.id}>
                    <td>{exam.id}</td>
                    <td>{exam.examTitle}</td>
                    <td>{exam.subjectName || exam.subjectId}</td>
                    <td>{exam.examDate}</td>
                    <td>{exam.durationMinutes} min</td>
                    <td>{exam.examType}</td>
                    <td><span className={`status-badge status-${(exam.status || '').toLowerCase()}`}>{exam.status}</span></td>
                    <td>
                      <div className="action-buttons">
                        <button onClick={() => handleEdit(exam)} className="btn btn-secondary btn-small">Edit</button>
                        <button onClick={() => handleDelete(exam.id)} className="btn btn-danger btn-small">Delete</button>
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
