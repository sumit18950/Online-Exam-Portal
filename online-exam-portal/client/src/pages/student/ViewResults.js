import { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import api from '../../services/api';
import { getUserId } from '../../utils/authUtil';
import './Student.css';

export const ViewResults = () => {
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const location = useLocation();
  const successMessage = location.state?.message || '';

  useEffect(() => {
    fetchResults();
  }, []);

  const fetchResults = async () => {
    const userId = getUserId();
    try {
      const response = await api.get(`/results/user/${userId}`);
      const data = Array.isArray(response.data) ? response.data : [];
      setResults(data);
      setError('');
    } catch (err) {
      if (err.response?.status === 404) {
        setResults([]);
      } else {
        setError(err.response?.data?.message || 'Failed to fetch results');
      }
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <div className="container"><div className="loading">Loading...</div></div>;

  return (
    <div className="container">
      <div className="student-card">
        <h2>My Results</h2>
        {successMessage && <div className="success-message">{successMessage}</div>}
        {error && <div className="error-message">{error}</div>}
        {results.length === 0 ? (
          <p className="no-data">No results available yet.</p>
        ) : (
          <div className="results-grid">
            {results.map((result) => (
              <div key={result.id} className="result-card">
                <h3>{result.examTitle || `Exam ${result.examId}`}</h3>
                <div className="result-details">
                  <div className="detail-item">
                    <span className="label">Score:</span>
                    <span className="value score-value">{result.score}</span>
                  </div>
                  <div className="detail-item">
                    <span className="label">Grade:</span>
                    <span className={`value grade-badge grade-${(result.grade || '').charAt(0).toLowerCase()}`}>
                      {result.grade || 'N/A'}
                    </span>
                  </div>
                  <div className="detail-item">
                    <span className="label">Evaluated:</span>
                    <span className="value">
                      {result.evaluatedAt ? new Date(result.evaluatedAt).toLocaleString() : 'N/A'}
                    </span>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};
