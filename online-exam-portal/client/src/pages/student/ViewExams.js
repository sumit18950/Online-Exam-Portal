import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../services/api';
import { getUserId } from '../../utils/authUtil';
import { Alert, Button, Card, Loader } from '../../components/ui';
import { RoleSidebar } from '../../components/RoleSidebar';
import './Student.css';

export const ViewExams = () => {
  const [exams, setExams] = useState([]);
  const [attendedExamIds, setAttendedExamIds] = useState(new Set());
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    fetchExams();
  }, []);

  const fetchExams = async () => {
    try {
      const userId = getUserId();
      const [examsRes, resultsRes] = await Promise.all([
        api.get('/api/exams'),
        api.get(`/results/user/${userId}`).catch(() => ({ data: [] })),
      ]);
      const data = Array.isArray(examsRes.data) ? examsRes.data : (examsRes.data?.data || []);
      setExams(data);

      const results = Array.isArray(resultsRes.data) ? resultsRes.data : [];
      const attended = new Set(results.map((r) => r.examId));
      setAttendedExamIds(attended);

      setError('');
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to fetch exams');
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="role-layout">
        <RoleSidebar role="STUDENT" />
        <main className="role-main"><Card><Loader text="Loading exams..." /></Card></main>
      </div>
    );
  }

  return (
    <div className="role-layout">
      <RoleSidebar role="STUDENT" />

      <main className="role-main">
        <Card title="Available Exams" subtitle="Check schedules, duration, and exam status before attempting.">
          <Alert type="error">{error}</Alert>
          {exams.length === 0 ? (
            <p className="no-data">No exams available.</p>
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

                  {attendedExamIds.has(exam.id) ? (
                    <Button variant="secondary" fullWidth disabled>Attended</Button>
                  ) : (
                    <Button
                      onClick={() => navigate(`/student/exams/${exam.id}/attempt`)}
                      fullWidth
                      disabled={exam.status === 'COMPLETED'}
                    >
                      {exam.status === 'COMPLETED' ? 'Exam Completed' : 'Attempt Exam'}
                    </Button>
                  )}
                </div>
              ))}
            </div>
          )}
        </Card>
      </main>
    </div>
  );
};
