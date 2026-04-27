import { useEffect, useState } from 'react';
import { Alert, Card, Loader } from '../../components/ui';
import { RoleSidebar } from '../../components/RoleSidebar';
import api from '../../services/api';
import './Teacher.css';

export const TeacherResults = () => {
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

  if (loading) {
    return (
      <div className="role-layout">
        <RoleSidebar role="TEACHER" />
        <main className="role-main"><Card><Loader text="Loading results..." /></Card></main>
      </div>
    );
  }

  return (
    <div className="role-layout">
      <RoleSidebar role="TEACHER" />

      <main className="role-main">
        <Card title="Student Results" subtitle="Review exam outcomes across your classes.">
          <Alert type="error">{error}</Alert>
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
        </Card>
      </main>
    </div>
  );
};
