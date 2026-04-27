import { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import api from '../../services/api';
import { getUserId } from '../../utils/authUtil';
import { Alert, Button, Card, Loader } from '../../components/ui';
import { RoleSidebar } from '../../components/RoleSidebar';
import './Student.css';

export const ViewResults = () => {
  const [results, setResults] = useState([]);
  const [leaderboard, setLeaderboard] = useState([]);
  const [selectedExamId, setSelectedExamId] = useState('');
  const [loading, setLoading] = useState(true);
  const [leaderboardLoading, setLeaderboardLoading] = useState(false);
  const [error, setError] = useState('');
  const [actionError, setActionError] = useState('');
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
      if (data.length > 0 && data[0].examId) {
        setSelectedExamId(String(data[0].examId));
      }
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

  useEffect(() => {
    if (!selectedExamId) {
      setLeaderboard([]);
      return;
    }

    const fetchLeaderboard = async () => {
      setLeaderboardLoading(true);
      try {
        const response = await api.get(`/results/leaderboard/${selectedExamId}`);
        const data = Array.isArray(response.data) ? response.data : [];
        setLeaderboard(data);
      } catch (err) {
        setLeaderboard([]);
      } finally {
        setLeaderboardLoading(false);
      }
    };

    fetchLeaderboard();
  }, [selectedExamId]);

  const handleDownloadCertificate = async (resultId) => {
    setActionError('');
    try {
      const response = await api.get(`/results/certificate/${resultId}`);
      const cert = response.data || {};
      const certText = [
        'Exam Certificate',
        '----------------',
        `Student: ${cert.studentName || 'N/A'}`,
        `Exam: ${cert.examTitle || 'N/A'}`,
        `Score: ${cert.score ?? 'N/A'}`,
        `Grade: ${cert.grade || 'N/A'}`,
        `Status: ${cert.resultStatus || 'N/A'}`,
        cert.certificateText || '',
      ].join('\n');

      const blob = new Blob([certText], { type: 'text/plain;charset=utf-8' });
      const link = document.createElement('a');
      link.href = URL.createObjectURL(blob);
      link.download = `certificate-result-${resultId}.txt`;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      URL.revokeObjectURL(link.href);
    } catch (err) {
      setActionError('Could not download certificate for this result.');
    }
  };

  if (loading) {
    return (
      <div className="role-layout">
        <RoleSidebar role="STUDENT" />
        <main className="role-main"><Card><Loader text="Loading results..." /></Card></main>
      </div>
    );
  }

  return (
    <div className="role-layout">
      <RoleSidebar role="STUDENT" />

      <main className="role-main">
        <Card title="My Results" subtitle="Track your score, grade, and performance by exam.">
          <Alert type="success">{successMessage}</Alert>
          <Alert type="error">{error || actionError}</Alert>

          {results.length === 0 ? (
            <p className="no-data">No results available yet.</p>
          ) : (
            <>
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

                    <div className="result-actions">
                      <Button variant="ghost" onClick={() => handleDownloadCertificate(result.id)}>
                        Download Certificate
                      </Button>
                    </div>
                  </div>
                ))}
              </div>

              <section className="leaderboard-section">
                <div className="results-toolbar">
                  <h3>Leaderboard</h3>
                  <select
                    className="exam-selector"
                    value={selectedExamId}
                    onChange={(e) => setSelectedExamId(e.target.value)}
                  >
                    {results.map((result) => (
                      <option key={result.id} value={result.examId}>{result.examTitle || `Exam ${result.examId}`}</option>
                    ))}
                  </select>
                </div>

                {leaderboardLoading ? (
                  <Loader text="Loading leaderboard..." />
                ) : leaderboard.length === 0 ? (
                  <p className="no-data">Leaderboard is not available for this exam yet.</p>
                ) : (
                  <div className="table-responsive">
                    <table className="users-table leaderboard-table">
                      <thead>
                        <tr>
                          <th>Rank</th>
                          <th>Student</th>
                          <th>Score</th>
                          <th>Grade</th>
                        </tr>
                      </thead>
                      <tbody>
                        {leaderboard.map((item, index) => (
                          <tr key={`${item.username}-${index}`}>
                            <td>{index + 1}</td>
                            <td>{item.username}</td>
                            <td>{item.score}</td>
                            <td>
                              <span className={`grade-badge grade-${(item.grade || '').charAt(0).toLowerCase()}`}>
                                {item.grade || 'N/A'}
                              </span>
                            </td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </div>
                )}
              </section>
            </>
          )}
        </Card>
      </main>
    </div>
  );
};
