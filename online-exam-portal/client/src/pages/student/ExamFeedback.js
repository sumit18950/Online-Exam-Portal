import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../../services/api';
import { getUserId } from '../../utils/authUtil';
import './Student.css';

export const ExamFeedback = () => {
  const { examId } = useParams();
  const userId = getUserId();
  const [feedback, setFeedback] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    const fetchFeedback = async () => {
      try {
        const res = await api.get(`/answers/feedback?examId=${examId}&userId=${userId}`);
        setFeedback(res.data);
      } catch (err) {
        setError(err.response?.data?.message || err.response?.data || 'Failed to load feedback');
      } finally {
        setLoading(false);
      }
    };
    fetchFeedback();
  }, [examId, userId]);

  if (loading) return <div className="container"><div className="loading">Loading feedback...</div></div>;
  if (error) return <div className="container"><div className="error-message">{error}</div></div>;
  if (!feedback) return <div className="container"><p className="no-data">No feedback available.</p></div>;

  const percentage = feedback.totalScore > 0
    ? Math.round((feedback.obtainedScore / feedback.totalScore) * 100)
    : 0;

  return (
    <div className="container">
      <div className="feedback-card">
        <button onClick={() => navigate('/student/results')} className="btn btn-secondary feedback-back-btn">Back to Results</button>

        <div className="feedback-header">
          <h2>{feedback.examTitle}</h2>
          <div className="feedback-summary">
            <div className="feedback-stat">
              <span className="feedback-stat-value">{feedback.obtainedScore}/{feedback.totalScore}</span>
              <span className="feedback-stat-label">Score</span>
            </div>
            <div className="feedback-stat">
              <span className="feedback-stat-value">{percentage}%</span>
              <span className="feedback-stat-label">Percentage</span>
            </div>
            <div className="feedback-stat">
              <span className={`feedback-stat-value grade-text grade-text-${(feedback.grade || '').charAt(0).toLowerCase()}`}>{feedback.grade}</span>
              <span className="feedback-stat-label">Grade</span>
            </div>
            <div className="feedback-stat">
              <span className="feedback-stat-value">{feedback.questions.filter(q => q.isCorrect).length}/{feedback.questions.length}</span>
              <span className="feedback-stat-label">Correct</span>
            </div>
          </div>
        </div>

        <div className="feedback-questions">
          {feedback.questions.map((q, idx) => (
            <div key={q.questionId} className={`feedback-question ${q.isCorrect ? 'feedback-correct' : 'feedback-wrong'}`}>
              <div className="feedback-q-header">
                <span className="feedback-q-number">Q{idx + 1}</span>
                <span className={`feedback-q-badge ${q.isCorrect ? 'badge-correct' : 'badge-wrong'}`}>
                  {q.isCorrect ? 'Correct' : (q.selectedOptionId ? 'Wrong' : 'Not Answered')}
                </span>
                <span className="feedback-q-marks">{q.isCorrect ? q.marks : 0}/{q.marks} marks</span>
              </div>
              <p className="feedback-q-text">{q.questionText}</p>
              <div className="feedback-options">
                {q.options.map((opt) => {
                  const isSelected = opt.optionId === q.selectedOptionId;
                  const isCorrectOpt = opt.isCorrect;
                  let className = 'feedback-option';
                  if (isCorrectOpt) className += ' option-correct';
                  if (isSelected && !isCorrectOpt) className += ' option-wrong';
                  if (isSelected) className += ' option-selected';

                  return (
                    <div key={opt.optionId} className={className}>
                      <span className="feedback-option-marker">
                        {isCorrectOpt && isSelected && '\u2713'}
                        {isCorrectOpt && !isSelected && '\u2713'}
                        {!isCorrectOpt && isSelected && '\u2717'}
                      </span>
                      <span className="feedback-option-text">{opt.optionText}</span>
                      {isSelected && <span className="feedback-option-tag tag-selected">Your Answer</span>}
                      {isCorrectOpt && <span className="feedback-option-tag tag-correct">Correct Answer</span>}
                    </div>
                  );
                })}
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};
