import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../services/api';
import './Student.css';

export const ViewExams = () => {
  const [exams, setExams] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    fetchExams();
  }, []);

  const fetchExams = async () => {
    try {
      const response = await api.get('/api/exams');
      const data = Array.isArray(response.data) ? response.data : (response.data?.data || []);
      setExams(data);
      setError('');
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to fetch exams');
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <div className="container"><div className="loading">Loading...</div></div>;

  return (
    <div className="container">
      <div className="student-card">
        <h2>Available Exams</h2>
        {error && <div className="error-message">{error}</div>}
        {exams.length === 0 ? (
          <p className="no-data">No exams available</p>
        ) : (
          <div className="exams-grid">
            {exams.map((exam) => (
              <div key={exam.id} className="exam-card">
                <h3>{exam.examTitle}</h3>
                <div className="exam-details">
                  <div className="detail-item">
                    <span className="label">Subject:</span>
                    <span className="value">{exam.subjectName || 'N/A'}</span>
                  </div>
                  <div className="detail-item">
                    <span className="label">Date:</span>
                    <span className="value">{exam.examDate || 'N/A'}</span>
                  </div>
                  <div className="detail-item">
                    <span className="label">Time:</span>
                    <span className="value">{exam.examTime || 'N/A'}</span>
                  </div>
                  <div className="detail-item">
                    <span className="label">Duration:</span>
                    <span className="value">{exam.durationMinutes || 'N/A'} min</span>
                  </div>
                  <div className="detail-item">
                    <span className="label">Type:</span>
                    <span className="value">{exam.examType || 'N/A'}</span>
                  </div>
                  <div className="detail-item">
                    <span className="label">Status:</span>
                    <span className={`value status-badge status-${(exam.status || '').toLowerCase()}`}>{exam.status || 'N/A'}</span>
                  </div>
                </div>
                <button
                  onClick={() => navigate(`/student/exams/${exam.id}/attempt`)}
                  className="btn btn-primary btn-full"
                  disabled={exam.status === 'COMPLETED'}
                >
                  {exam.status === 'COMPLETED' ? 'Exam Completed' : 'Attempt Exam'}
                </button>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};
