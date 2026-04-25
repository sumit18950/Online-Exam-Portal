import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../services/api';
import './Teacher.css';

export const ManageQuestions = () => {
  const [subjects, setSubjects] = useState([]);
  const [exams, setExams] = useState([]);
  const [filteredExams, setFilteredExams] = useState([]);
  const [selectedSubject, setSelectedSubject] = useState('');
  const [selectedExam, setSelectedExam] = useState('');
  const [questions, setQuestions] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [loading, setLoading] = useState(true);
  const [questionsLoading, setQuestionsLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  // Edit state
  const [editingId, setEditingId] = useState(null);
  const [editForm, setEditForm] = useState({ questionText: '', questionType: '', marks: 1, options: [] });
  const [saving, setSaving] = useState(false);

  const navigate = useNavigate();

  useEffect(() => { fetchData(); }, []);

  const fetchData = async () => {
    try {
      const [subjectsRes, examsRes] = await Promise.all([
        api.get('/api/exams/subjects'),
        api.get('/api/exams'),
      ]);
      setSubjects(Array.isArray(subjectsRes.data) ? subjectsRes.data : []);
      setExams(Array.isArray(examsRes.data) ? examsRes.data : []);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to fetch data');
    } finally {
      setLoading(false);
    }
  };

  const handleSubjectChange = (value) => {
    setSelectedSubject(value);
    setSelectedExam('');
    setQuestions([]);
    cancelEdit();
    if (value) {
      setFilteredExams(exams.filter((e) => e.subjectId === parseInt(value)));
    } else {
      setFilteredExams([]);
    }
  };

  const handleExamChange = (value) => {
    setSelectedExam(value);
    cancelEdit();
    if (value) { fetchQuestions(value); } else { setQuestions([]); }
  };

  const fetchQuestions = async (examId) => {
    setQuestionsLoading(true);
    setError('');
    try {
      const response = await api.get(`/api/questions/exam/${examId}`);
      setQuestions(Array.isArray(response.data) ? response.data : []);
    } catch (err) {
      if (err.response?.status === 404) { setQuestions([]); }
      else { setError(err.response?.data?.message || 'Failed to fetch questions'); }
    } finally {
      setQuestionsLoading(false);
    }
  };

  const handleDelete = async (questionId) => {
    if (window.confirm('Are you sure you want to delete this question?')) {
      try {
        await api.delete(`/api/questions/subject/${selectedSubject}/question/${questionId}`);
        setQuestions(questions.filter((q) => q.id !== questionId));
        setSuccess('Question deleted successfully!');
        setTimeout(() => setSuccess(''), 3000);
      } catch (err) {
        setError(err.response?.data?.message || 'Failed to delete question');
      }
    }
  };

  // Edit functions
  const startEdit = (question) => {
    setEditingId(question.id);
    setEditForm({
      questionText: question.questionText || '',
      questionType: question.questionType || 'MULTIPLE_CHOICE',
      marks: question.marks || 1,
      options: question.options ? question.options.map((o) => ({ optionText: o.optionText, isCorrect: o.isCorrect })) : [],
    });
    setError('');
    setSuccess('');
  };

  const cancelEdit = () => {
    setEditingId(null);
    setEditForm({ questionText: '', questionType: '', marks: 1, options: [] });
  };

  const handleEditOptionChange = (index, field, value) => {
    setEditForm((prev) => {
      const newOptions = [...prev.options];
      if (field === 'isCorrect') {
        newOptions.forEach((opt, i) => { opt.isCorrect = i === index; });
      } else {
        newOptions[index] = { ...newOptions[index], [field]: value };
      }
      return { ...prev, options: newOptions };
    });
  };

  const addEditOption = () => {
    setEditForm((prev) => ({ ...prev, options: [...prev.options, { optionText: '', isCorrect: false }] }));
  };

  const removeEditOption = (index) => {
    setEditForm((prev) => {
      const newOptions = prev.options.filter((_, i) => i !== index);
      if (prev.options[index].isCorrect && newOptions.length > 0) { newOptions[0].isCorrect = true; }
      return { ...prev, options: newOptions };
    });
  };

  const saveEdit = async () => {
    if (editForm.options.length < 2) { setError('At least 2 options are required'); return; }
    if (!editForm.questionText.trim()) { setError('Question text is required'); return; }
    setSaving(true);
    setError('');

    const payload = {
      questionText: editForm.questionText,
      questionType: editForm.questionType,
      marks: parseInt(editForm.marks),
      subject: { id: parseInt(selectedSubject) },
      exam: { id: parseInt(selectedExam) },
      options: editForm.options.map((opt) => ({ optionText: opt.optionText, isCorrect: opt.isCorrect })),
    };

    try {
      const res = await api.put(`/api/questions/${editingId}`, payload);
      setQuestions(questions.map((q) => (q.id === editingId ? res.data : q)));
      setSuccess('Question updated successfully!');
      cancelEdit();
      setTimeout(() => setSuccess(''), 3000);
    } catch (err) {
      const p = err.response?.data;
      setError(typeof p === 'string' ? p : p?.message || 'Failed to update question');
    } finally {
      setSaving(false);
    }
  };

  if (loading) return <div className="container"><div className="loading">Loading...</div></div>;

  return (
    <div className="container">
      <div className="teacher-card">
        <div className="card-header">
          <h2>Manage Questions</h2>
          <div className="header-actions">
            <input type="text" className="search-input" placeholder="Search questions..." value={searchTerm} onChange={(e) => setSearchTerm(e.target.value)} />
            <button onClick={() => navigate('/teacher/questions/create')} className="btn btn-primary">Add New Question</button>
          </div>
        </div>
        {error && <div className="error-message">{error}</div>}
        {success && <div className="success-message">{success}</div>}

        <div className="form-row">
          <div className="form-group">
            <label htmlFor="subjectSelect">Select Subject</label>
            <select id="subjectSelect" value={selectedSubject} onChange={(e) => handleSubjectChange(e.target.value)}>
              <option value="">-- Choose a Subject --</option>
              {subjects.map((s) => <option key={s.id} value={s.id}>{s.subjectName}</option>)}
            </select>
          </div>
          <div className="form-group">
            <label htmlFor="examSelect">Select Exam</label>
            <select id="examSelect" value={selectedExam} onChange={(e) => handleExamChange(e.target.value)} disabled={!selectedSubject}>
              <option value="">{selectedSubject ? '-- Choose an Exam --' : '-- Select a subject first --'}</option>
              {filteredExams.map((ex) => <option key={ex.id} value={ex.id}>{ex.examTitle}</option>)}
            </select>
          </div>
        </div>

        {questionsLoading && <div className="loading">Loading questions...</div>}

        {!questionsLoading && selectedExam && questions.length === 0 && (
          <p className="no-data">No questions found for this exam.</p>
        )}

        {!questionsLoading && questions.length > 0 && (
          <div className="questions-list">
            {questions.filter((question) => {
              if (!searchTerm.trim()) return true;
              const term = searchTerm.toLowerCase();
              return (question.questionText || '').toLowerCase().includes(term);
            }).map((question) => (
              <div key={question.id} className="question-item">
                {editingId === question.id ? (
                  /* ---- EDIT MODE ---- */
                  <div className="edit-question-form">
                    <div className="form-group">
                      <label>Question Text</label>
                      <textarea value={editForm.questionText} onChange={(e) => setEditForm((p) => ({ ...p, questionText: e.target.value }))} rows="3" disabled={saving} />
                    </div>
                    <div className="form-row">
                      <div className="form-group">
                        <label>Type</label>
                        <select value={editForm.questionType} onChange={(e) => setEditForm((p) => ({ ...p, questionType: e.target.value }))} disabled={saving}>
                          <option value="MULTIPLE_CHOICE">Multiple Choice</option>
                        </select>
                      </div>
                      <div className="form-group">
                        <label>Marks</label>
                        <input type="number" value={editForm.marks} onChange={(e) => setEditForm((p) => ({ ...p, marks: e.target.value }))} min="1" disabled={saving} />
                      </div>
                    </div>
                    <div className="section-header">
                      <h4 className="section-title">Options</h4>
                      <button type="button" onClick={addEditOption} className="btn btn-primary btn-small" disabled={saving}>+ Add Option</button>
                    </div>
                    {editForm.options.map((opt, idx) => (
                      <div key={idx} className="option-row">
                        <input type="radio" name={`edit-correct-${question.id}`} checked={opt.isCorrect} onChange={() => handleEditOptionChange(idx, 'isCorrect', true)} disabled={saving} />
                        <input type="text" value={opt.optionText} onChange={(e) => handleEditOptionChange(idx, 'optionText', e.target.value)} placeholder={`Option ${idx + 1}`} disabled={saving} />
                        {opt.isCorrect && <span className="correct-label">Correct</span>}
                        {editForm.options.length > 2 && (
                          <button type="button" onClick={() => removeEditOption(idx)} className="btn-icon btn-icon-danger" disabled={saving} title="Remove">&times;</button>
                        )}
                      </div>
                    ))}
                    <div className="button-group" style={{ justifyContent: 'flex-start' }}>
                      <button onClick={saveEdit} disabled={saving} className="btn btn-primary btn-small">{saving ? 'Saving...' : 'Save'}</button>
                      <button onClick={cancelEdit} disabled={saving} className="btn btn-secondary btn-small">Cancel</button>
                    </div>
                  </div>
                ) : (
                  /* ---- VIEW MODE ---- */
                  <>
                    <div className="question-header">
                      <h3>Q{question.id}: {question.questionText}</h3>
                      <span className="question-meta">Type: {question.questionType || 'N/A'} | Marks: {question.marks || 0}</span>
                    </div>
                    <div className="question-body">
                      {question.options && question.options.map((opt, idx) => (
                        <p key={opt.id || idx} className={opt.isCorrect ? 'correct-option' : ''}>
                          <strong>{idx + 1}.</strong> {opt.optionText} {opt.isCorrect && <span className="answer-highlight">&#10003; Correct</span>}
                        </p>
                      ))}
                    </div>
                    <div className="question-footer">
                      <div className="action-buttons">
                        <button onClick={() => startEdit(question)} className="btn btn-primary btn-small">Edit</button>
                        <button onClick={() => handleDelete(question.id)} className="btn btn-danger btn-small">Delete</button>
                      </div>
                    </div>
                  </>
                )}
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};
