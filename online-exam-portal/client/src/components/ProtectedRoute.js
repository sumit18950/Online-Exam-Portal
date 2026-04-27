import { useContext } from 'react';
import { Navigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';
import { Loader } from './ui';

export const ProtectedRoute = ({ children, requiredRole = null }) => {
  const { isAuthenticated, role, loading } = useContext(AuthContext);

  if (loading) {
    return <Loader text="Checking session..." />;
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" />;
  }

  if (requiredRole && role !== requiredRole) {
    return <Navigate to="/unauthorized" />;
  }

  return children;
};
