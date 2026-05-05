import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../../services/api';
import { getUserId, getUserName } from '../../utils/authUtil';
import './Student.css';

export const Leaderboard = () => {
  const { examId } = useParams();
  const navigate = useNavigate();
  const [results, setResults] = useState([]);
  const [exams, setExams] = useState([]);
  const [selectedExam, setSelectedExam] = useState(examId || '');
  const [leaderboard, setLeaderboard] = useState([]);
  const [loading, setLoading] = useState(true);
  const [lbLoading, setLbLoading] = useState(false);
  const [error, setError] = useState('');
  const currentUsername = getUserName();
  const userId = getUserId();

  useEffect(() => {
    const fetchUserResults = async () => {
      try {
        const res = await api.get(`/results/user/${userId}`);
        const data = Array.isArray(res.data) ? res.data : [];
        setResults(data);

        const uniqueExams = [];
        const seen = new Set();
        data.forEach((r) => {
          if (r.examId && !seen.has(r.examId)) {
            seen.add(r.examId);
            uniqueExams.push({ id: r.examId, title: r.examTitle || `Exam ${r.examId}` });
          }
        });
        setExams(uniqueExams);
      } catch (err) {
        setError('Failed to load your exams');
      } finally {
        setLoading(false);
      }
    };
    fetchUserResults();
  }, [userId]);

  useEffect(() => {
    if (selectedExam) {
      fetchLeaderboard(selectedExam);
    } else {
      setLeaderboard([]);
    }
  }, [selectedExam]);

  const fetchLeaderboard = async (eid) => {
    setLbLoading(true);
    setError('');
    try {
      const res = await api.get(`/results/leaderboard/${eid}`);
      setLeaderboard(Array.isArray(res.data) ? res.data : []);
    } catch (err) {
      setError('Failed to load leaderboard');
      setLeaderboard([]);
    } finally {
      setLbLoading(false);
    }
  };

  const currentExamTitle = exams.find((e) => String(e.id) === String(selectedExam))?.title || '';

  const myRank = leaderboard.findIndex(
    (entry) => entry.username === currentUsername
  ) + 1;

  if (loading) return <div className="container"><div className="loading">Loading...</div></div>;

  return (
    <div className="container">
      <div className="student-card">
        <div className="card-header">
          <h2>Leaderboard</h2>
          <button onClick={() => navigate('/student/results')} className="btn btn-secondary">Back to Results</button>
        </div>

        <div className="form-group" style={{ maxWidth: 400, marginBottom: '1.5rem' }}>
          <label htmlFor="examSelect">Select Exam</label>
          <select
            id="examSelect"
            value={selectedExam}
            onChange={(e) => setSelectedExam(e.target.value)}
          >
            <option value="">-- Choose an exam --</option>
            {exams.map((ex) => (
              <option key={ex.id} value={ex.id}>{ex.title}</option>
            ))}
          </select>
        </div>

        {error && <div className="error-message">{error}</div>}

        {!selectedExam && (
          <p className="no-data">Select an exam to view the leaderboard.</p>
        )}

        {selectedExam && lbLoading && (
          <div className="loading">Loading leaderboard...</div>
        )}

        {selectedExam && !lbLoading && leaderboard.length === 0 && !error && (
          <p className="no-data">No results available for this exam yet.</p>
        )}

        {selectedExam && !lbLoading && leaderboard.length > 0 && (
          <>
            {myRank > 0 && (
              <div className="lb-my-rank">
                <span className="lb-my-rank-label">Your Position</span>
                <span className="lb-my-rank-value">#{myRank}</span>
                <span className="lb-my-rank-of">out of {leaderboard.length} students</span>
              </div>
            )}

            <div className="table-responsive">
              <table className="users-table lb-table">
                <thead>
                  <tr>
                    <th>Rank</th>
                    <th>Student</th>
                    <th>Score</th>
                    <th>Grade</th>
                  </tr>
                </thead>
                <tbody>
                  {leaderboard.map((entry, idx) => {
                    const rank = idx + 1;
                    const isMe = entry.username === currentUsername;
                    return (
                      <tr key={idx} className={isMe ? 'lb-row-me' : ''}>
                        <td>
                          <span className={`lb-rank ${rank <= 3 ? `lb-rank-${rank}` : ''}`}>
                            {rank <= 3 ? ['', '\uD83E\uDD47', '\uD83E\uDD48', '\uD83E\uDD49'][rank] : `#${rank}`}
                          </span>
                        </td>
                        <td>
                          <span className="lb-username">
                            {entry.username}
                            {isMe && <span className="lb-you-tag">You</span>}
                          </span>
                        </td>
                        <td><span className="lb-score">{entry.score}</span></td>
                        <td>
                          <span className={`grade-badge grade-${(entry.grade || '').charAt(0).toLowerCase()}`}>
                            {entry.grade || 'N/A'}
                          </span>
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          </>
        )}
      </div>
    </div>
  );
};
