import { useEffect, useState } from 'react';
import api from '../../services/api';
import './Admin.css';

export const AdminResults = () => {
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchResults();
  }, []);

  const fetchResults = async () => {
    try {
      const response = await api.get('/results');
      const data = Array.isArray(response.data) ? response.data : [];
      setResults(data);
      setError('');
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to fetch results');
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <div className="container"><div className="loading">Loading...</div></div>;

  return (
    <div className="container">
      <div className="admin-card">
        <h2>Student Results</h2>
        {error && <div className="error-message">{error}</div>}
        {results.length === 0 ? (
          <p className="no-data">No results available yet.</p>
        ) : (
          <div className="table-responsive">
            <table className="users-table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Student</th>
                  <th>Exam</th>
                  <th>Score</th>
                  <th>Grade</th>
                  <th>Evaluated At</th>
                </tr>
              </thead>
              <tbody>
                {results.map((result) => (
                  <tr key={result.id}>
                    <td>{result.id}</td>
                    <td>{result.username || `User ${result.userId}`}</td>
                    <td>{result.examTitle || `Exam ${result.examId}`}</td>
                    <td><strong>{result.score}</strong></td>
                    <td><span className={`grade-badge grade-${(result.grade || '').charAt(0).toLowerCase()}`}>{result.grade || 'N/A'}</span></td>
                    <td>{result.evaluatedAt ? new Date(result.evaluatedAt).toLocaleString() : 'N/A'}</td>
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
