import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../services/api';
import './Teacher.css';

export const CreateQuestion = () => {
  const [subjects, setSubjects] = useState([]);
  const [selectedSubject, setSelectedSubject] = useState('');
  const [formData, setFormData] = useState({
    questionText: '',
    questionType: 'MULTIPLE_CHOICE',
    marks: 1,
    options: [
      { optionText: '', isCorrect: true },
      { optionText: '', isCorrect: false },
      { optionText: '', isCorrect: false },
      { optionText: '', isCorrect: false },
    ],
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    const fetchSubjects = async () => {
      try {
        const response = await api.get('/api/exams/subjects');
        setSubjects(Array.isArray(response.data) ? response.data : []);
      } catch (err) {
        setError('Failed to load subjects');
      }
    };
    fetchSubjects();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleOptionChange = (index, field, value) => {
    setFormData((prev) => {
      const newOptions = [...prev.options];
      if (field === 'isCorrect') {
        newOptions.forEach((opt, i) => { opt.isCorrect = i === index; });
      } else {
        newOptions[index] = { ...newOptions[index], [field]: value };
      }
      return { ...prev, options: newOptions };
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!selectedSubject) {
      setError('Please select a subject');
      return;
    }
    setError('');
    setSuccess('');
    setLoading(true);

    const payload = {
      questionText: formData.questionText,
      questionType: formData.questionType,
      marks: parseInt(formData.marks),
      subject: { id: parseInt(selectedSubject) },
      options: formData.options.map((opt) => ({
        optionText: opt.optionText,
        isCorrect: opt.isCorrect,
      })),
    };

    try {
      await api.post('/api/questions/upload', payload);
      setSuccess('Question created successfully!');
      setFormData({
        questionText: '',
        questionType: 'MULTIPLE_CHOICE',
        marks: 1,
        options: [
          { optionText: '', isCorrect: true },
          { optionText: '', isCorrect: false },
          { optionText: '', isCorrect: false },
          { optionText: '', isCorrect: false },
        ],
      });
      setTimeout(() => navigate('/teacher/questions'), 2000);
    } catch (err) {
      const payload2 = err.response?.data;
      setError(typeof payload2 === 'string' ? payload2 : payload2?.message || 'Failed to create question');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container">
      <div className="form-card">
        <h2>Create Question</h2>
        {error && <div className="error-message">{error}</div>}
        {success && <div className="success-message">{success}</div>}
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="subjectId">Subject</label>
            <select id="subjectId" value={selectedSubject} onChange={(e) => setSelectedSubject(e.target.value)} required disabled={loading}>
              <option value="">Select Subject</option>
              {subjects.map((s) => <option key={s.id} value={s.id}>{s.subjectName}</option>)}
            </select>
          </div>
          <div className="form-group">
            <label htmlFor="questionText">Question Text</label>
            <textarea id="questionText" name="questionText" value={formData.questionText} onChange={handleChange} required disabled={loading} rows="4" />
          </div>
          <div className="form-row">
            <div className="form-group">
              <label htmlFor="questionType">Question Type</label>
              <select id="questionType" name="questionType" value={formData.questionType} onChange={handleChange} disabled={loading}>
                <option value="MULTIPLE_CHOICE">Multiple Choice</option>
                <option value="DESCRIPTIVE">Descriptive</option>
              </select>
            </div>
            <div className="form-group">
              <label htmlFor="marks">Marks</label>
              <input type="number" id="marks" name="marks" value={formData.marks} onChange={handleChange} min="1" required disabled={loading} />
            </div>
          </div>

          <h3 className="section-title">Options</h3>
          {formData.options.map((opt, idx) => (
            <div key={idx} className="option-row">
              <input
                type="radio"
                name="correctOption"
                checked={opt.isCorrect}
                onChange={() => handleOptionChange(idx, 'isCorrect', true)}
                disabled={loading}
                title="Mark as correct"
              />
              <input
                type="text"
                placeholder={`Option ${idx + 1}`}
                value={opt.optionText}
                onChange={(e) => handleOptionChange(idx, 'optionText', e.target.value)}
                required
                disabled={loading}
              />
              {opt.isCorrect && <span className="correct-label">Correct</span>}
            </div>
          ))}

          <div className="button-group">
            <button type="submit" disabled={loading} className="btn btn-primary">
              {loading ? 'Creating...' : 'Create Question'}
            </button>
            <button type="button" onClick={() => navigate('/teacher/questions')} className="btn btn-secondary">Cancel</button>
          </div>
        </form>
      </div>
    </div>
  );
};
