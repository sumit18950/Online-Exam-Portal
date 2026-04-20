import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../services/api';
import './Admin.css';

export const AdminManageQuestions = () => {
  const [subjects, setSubjects] = useState([]);
  const [exams, setExams] = useState([]);
  const [filteredExams, setFilteredExams] = useState([]);
  const [selectedSubject, setSelectedSubject] = useState('');
  const [selectedExam, setSelectedExam] = useState('');
  const [questions, setQuestions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [questionsLoading, setQuestionsLoading] = useState(false);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const [subjectsRes, examsRes] = await Promise.all([
        api.get('/api/exams/subjects'),
        api.get('/api/exams'),
      ]);
      setSubjects(Array.isArray(subjectsRes.data) ? subjectsRes.data : []);
      setExams(Array.isArray(examsRes.data) ? examsRes.data : []);
      setError('');
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
    if (value) {
      const subjectId = parseInt(value);
      setFilteredExams(exams.filter((e) => e.subjectId === subjectId));
    } else {
      setFilteredExams([]);
    }
  };

  const handleExamChange = (value) => {
    setSelectedExam(value);
    if (value) {
      fetchQuestions(value);
    } else {
      setQuestions([]);
    }
  };

  const fetchQuestions = async (examId) => {
    setQuestionsLoading(true);
    setError('');
    try {
      const response = await api.get(`/api/questions/exam/${examId}`);
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
      <div className="admin-card">
        <div className="card-header">
          <h2>Manage Questions</h2>
          <button onClick={() => navigate('/admin/questions/create')} className="btn btn-primary">Add New Question</button>
        </div>
        {error && <div className="error-message">{error}</div>}

        <div className="form-row">
          <div className="form-group">
            <label htmlFor="subjectSelect">Select Subject</label>
            <select id="subjectSelect" value={selectedSubject} onChange={(e) => handleSubjectChange(e.target.value)}>
              <option value="">-- Choose a Subject --</option>
              {subjects.map((s) => (
                <option key={s.id} value={s.id}>{s.subjectName}</option>
              ))}
            </select>
          </div>
          <div className="form-group">
            <label htmlFor="examSelect">Select Exam</label>
            <select id="examSelect" value={selectedExam} onChange={(e) => handleExamChange(e.target.value)} disabled={!selectedSubject}>
              <option value="">{selectedSubject ? '-- Choose an Exam --' : '-- Select a subject first --'}</option>
              {filteredExams.map((ex) => (
                <option key={ex.id} value={ex.id}>{ex.examTitle}</option>
              ))}
            </select>
          </div>
        </div>

        {questionsLoading && <div className="loading">Loading questions...</div>}

        {!questionsLoading && selectedExam && questions.length === 0 && (
          <p className="no-data">No questions found for this exam.</p>
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
