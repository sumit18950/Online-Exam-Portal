import { useCallback, useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../../services/api';
import { getUserId } from '../../utils/authUtil';
import './Student.css';

export const AttemptExam = () => {
  const { examId } = useParams();
  const userId = getUserId();
  const [questions, setQuestions] = useState([]);
  const [answers, setAnswers] = useState({});
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');
  const [currentQuestion, setCurrentQuestion] = useState(0);
  const [, setEnrolled] = useState(false);
  const navigate = useNavigate();

  const enrollAndFetchQuestions = useCallback(async () => {
    try {
      // Step 1: Enroll in the exam
      await api.post(`/exam/enroll?examId=${examId}&userId=${userId}`);
      setEnrolled(true);

      // Step 2: Fetch questions
      const result = await api.get(`/exam/questions/${examId}/${userId}`);
      const questionsData = Array.isArray(result.data) ? result.data : [];
      setQuestions(questionsData);

      const initialAnswers = {};
      questionsData.forEach((q) => { initialAnswers[q.questionId] = null; });
      setAnswers(initialAnswers);
      setError('');
    } catch (err) {
      const msg = err.response?.data;
      const errorMsg = typeof msg === 'string' ? msg : msg?.message || 'Failed to start exam';
      // If already enrolled, try fetching questions directly
      if (errorMsg.toLowerCase().includes('already enrolled') || err.response?.status === 409) {
        setEnrolled(true);
        try {
          const result = await api.get(`/exam/questions/${examId}/${userId}`);
          const questionsData = Array.isArray(result.data) ? result.data : [];
          setQuestions(questionsData);
          const initialAnswers = {};
          questionsData.forEach((q) => { initialAnswers[q.questionId] = null; });
          setAnswers(initialAnswers);
          setError('');
        } catch (err2) {
          setError('Failed to fetch questions');
        }
      } else {
        setError(errorMsg);
      }
    } finally {
      setLoading(false);
    }
  }, [examId, userId]);

  useEffect(() => {
    enrollAndFetchQuestions();
  }, [enrollAndFetchQuestions]);

  const handleAnswerChange = (questionId, optionId) => {
    setAnswers((prev) => ({ ...prev, [questionId]: optionId }));

    // Submit individual answer to backend
    api.post('/answers/selectanswer', {
      examId: parseInt(examId),
      questionId: questionId,
      userId: parseInt(userId),
      selectedOptionId: optionId,
      submittedAt: new Date().toISOString(),
    }).catch((err) => console.error('Failed to save answer:', err));
  };

  const handleFinish = async () => {
    if (window.confirm('Are you sure you want to finish the exam? This cannot be undone.')) {
      setSubmitting(true);
      try {
        const result = await api.post(`/answers/finish?examId=${examId}&userId=${userId}`);
        navigate('/student/results', { state: { message: 'Exam submitted successfully!', result: result.data } });
      } catch (err) {
        const msg = err.response?.data;
        setError(typeof msg === 'string' ? msg : msg?.message || 'Failed to submit exam');
        setSubmitting(false);
      }
    }
  };

  if (loading) return <div className="container"><div className="loading">Loading exam...</div></div>;
  if (error && questions.length === 0) return <div className="container"><div className="error-message">{error}</div></div>;
  if (questions.length === 0) return <div className="container"><p className="no-data">No questions found for this exam.</p></div>;

  const question = questions[currentQuestion];

  return (
    <div className="container">
      <div className="exam-attempt-card">
        <div className="exam-header">
          <h2>Exam Attempt</h2>
          <div className="question-counter">
            Question {currentQuestion + 1} of {questions.length}
          </div>
        </div>

        {error && <div className="error-message">{error}</div>}

        <div className="question-section">
          <h3>{question.questionText}</h3>
          <p className="question-meta-info">Type: {question.questionType || 'N/A'} | Marks: {question.marks || 0}</p>
          <div className="options">
            {question.options && question.options.map((opt) => (
              <label key={opt.optionId} className={`option-label ${answers[question.questionId] === opt.optionId ? 'selected' : ''}`}>
                <input
                  type="radio"
                  name={`question-${question.questionId}`}
                  value={opt.optionId}
                  checked={answers[question.questionId] === opt.optionId}
                  onChange={() => handleAnswerChange(question.questionId, opt.optionId)}
                  disabled={submitting}
                />
                <span>{opt.optionText}</span>
              </label>
            ))}
          </div>
        </div>

        <div className="navigation-buttons">
          <button
            onClick={() => setCurrentQuestion(Math.max(0, currentQuestion - 1))}
            disabled={currentQuestion === 0 || submitting}
            className="btn btn-secondary"
          >
            Previous
          </button>
          <span className="question-status">
            {Object.values(answers).filter((a) => a !== null).length} / {questions.length} answered
          </span>
          {currentQuestion === questions.length - 1 ? (
            <button onClick={handleFinish} disabled={submitting} className="btn btn-success">
              {submitting ? 'Submitting...' : 'Finish Exam'}
            </button>
          ) : (
            <button
              onClick={() => setCurrentQuestion(Math.min(questions.length - 1, currentQuestion + 1))}
              disabled={submitting}
              className="btn btn-primary"
            >
              Next
            </button>
          )}
        </div>

        <div className="question-navigator">
          <h4>Questions</h4>
          <div className="question-grid">
            {questions.map((q, idx) => (
              <button
                key={q.questionId}
                onClick={() => setCurrentQuestion(idx)}
                className={`question-btn ${idx === currentQuestion ? 'active' : ''} ${answers[q.questionId] !== null ? 'answered' : ''}`}
                disabled={submitting}
              >
                {idx + 1}
              </button>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};
