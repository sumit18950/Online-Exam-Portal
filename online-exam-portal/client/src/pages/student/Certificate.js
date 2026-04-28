import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../../services/api';
import './Student.css';

export const Certificate = () => {
  const { resultId } = useParams();
  const [cert, setCert] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    const fetchCertificate = async () => {
      try {
        const res = await api.get(`/results/certificate/${resultId}`);
        setCert(res.data);
      } catch (err) {
        setError(err.response?.data?.message || err.response?.data || 'Failed to load certificate');
      } finally {
        setLoading(false);
      }
    };
    fetchCertificate();
  }, [resultId]);

  if (loading) return <div className="container"><div className="loading">Loading certificate...</div></div>;
  if (error) return <div className="container"><div className="error-message">{error}</div></div>;
  if (!cert) return <div className="container"><p className="no-data">Certificate not available.</p></div>;

  const isPassed = cert.resultStatus === 'PASS';

  return (
    <div className="container">
      <button onClick={() => navigate('/student/results')} className="btn btn-secondary" style={{ marginBottom: '1rem' }}>Back to Results</button>
      <div className={`certificate ${isPassed ? 'certificate-pass' : 'certificate-fail'}`}>
        <div className="certificate-border">
          <div className="certificate-inner">
            <h3 className="certificate-subtitle">Online Exam Portal</h3>
            <h1 className="certificate-title">Certificate of {isPassed ? 'Achievement' : 'Participation'}</h1>
            <div className="certificate-divider"></div>
            <p className="certificate-text">This is to certify that</p>
            <h2 className="certificate-name">{cert.studentName}</h2>
            <p className="certificate-text">
              has {isPassed ? 'successfully passed' : 'attempted'} the exam
            </p>
            <h3 className="certificate-exam">{cert.examTitle}</h3>
            <div className="certificate-details">
              <div className="certificate-detail">
                <span className="certificate-detail-label">Score</span>
                <span className="certificate-detail-value">{cert.score}</span>
              </div>
              <div className="certificate-detail">
                <span className="certificate-detail-label">Grade</span>
                <span className="certificate-detail-value">{cert.grade}</span>
              </div>
              <div className="certificate-detail">
                <span className={`certificate-detail-value ${isPassed ? 'status-pass' : 'status-fail'}`}>{cert.resultStatus}</span>
                <span className="certificate-detail-label">Status</span>
              </div>
            </div>
            <div className="certificate-divider"></div>
            <div className="certificate-footer">
              {cert.examDate && <p>Exam Date: {new Date(cert.examDate).toLocaleDateString()}</p>}
              {cert.evaluatedAt && <p>Evaluated: {new Date(cert.evaluatedAt).toLocaleString()}</p>}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
