import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../services/api';
import './Teacher.css';

export const ManageQuestions = () => {
  const [subjects, setSubjects] = useState([]);
  const [selectedSubject, setSelectedSubject] = useState('');
  const [questions, setQuestions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [questionsLoading, setQuestionsLoading] = useState(false);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    fetchSubjects();
  }, []);

  const fetchSubjects = async () => {
    try {
      const response = await api.get('/api/exams/subjects');
      const data = Array.isArray(response.data) ? response.data : [];
      setSubjects(data);
      setError('');
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to fetch subjects');
    } finally {
      setLoading(false);
    }
  };

  const fetchQuestions = async (subjectId) => {
    setQuestionsLoading(true);
    setError('');
    try {
      const response = await api.get(`/api/questions/subject/${subjectId}`);
      const data = Array.isArray(response.data) ? response.data : [];
      setQuestions(data);
    } catch (err) {
      if (err.response?.status === 404) {
        setQuestions([]);
      } else {
        setError(err.response?.data?.message || 'Failed to fetch questions');
      }
    } finally {
      setQuestionsLoading(false);
    }
  };

  const handleSubjectChange = (e) => {
    const subjectId = e.target.value;
    setSelectedSubject(subjectId);
    if (subjectId) {
      fetchQuestions(subjectId);
    } else {
      setQuestions([]);
    }
  };

  const handleDelete = async (questionId) => {
    if (window.confirm('Are you sure you want to delete this question?')) {
      try {
        await api.delete(`/api/questions/subject/${selectedSubject}/question/${questionId}`);
        setQuestions(questions.filter((q) => q.id !== questionId));
      } catch (err) {
        setError(err.response?.data?.message || 'Failed to delete question');
      }
    }
  };

  if (loading) return <div className="container"><div className="loading">Loading...</div></div>;

  return (
    <div className="container">
      <div className="teacher-card">
        <div className="card-header">
          <h2>Manage Questions</h2>
          <button onClick={() => navigate('/teacher/questions/create')} className="btn btn-primary">Add New Question</button>
        </div>
        {error && <div className="error-message">{error}</div>}

        <div className="form-group">
          <label htmlFor="subjectSelect">Select Subject</label>
          <select id="subjectSelect" value={selectedSubject} onChange={handleSubjectChange}>
            <option value="">-- Choose a Subject --</option>
            {subjects.map((s) => (
              <option key={s.id} value={s.id}>{s.subjectName}</option>
            ))}
          </select>
        </div>

        {questionsLoading && <div className="loading">Loading questions...</div>}

        {!questionsLoading && selectedSubject && questions.length === 0 && (
          <p className="no-data">No questions found for this subject.</p>
        )}

        {!questionsLoading && questions.length > 0 && (
          <div className="questions-list">
            {questions.map((question) => (
              <div key={question.id} className="question-item">
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
                  <button onClick={() => handleDelete(question.id)} className="btn btn-danger btn-small">Delete</button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};
