import { createContext, useState, useEffect } from 'react';
import { getToken, getRole, getUserId, getUserName, clearAuth, extractRole } from '../utils/authUtil';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [role, setRole] = useState(null);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = getToken();
    const storedRole = getRole();
    if (token && storedRole) {
      setIsAuthenticated(true);
      setRole(storedRole);
      setUser({
        id: getUserId(),
        username: getUserName(),
        role: storedRole,
      });
    }
    setLoading(false);
  }, []);

  const login = (token, userData) => {
    localStorage.setItem('jwt_token', token);
    const userRole = extractRole(userData?.role) || 'STUDENT';
    localStorage.setItem('user_role', userRole);
    localStorage.setItem('user_id', userData?.id || '');
    localStorage.setItem('user_name', userData?.username || '');
    setIsAuthenticated(true);
    setRole(userRole);
    setUser({ ...userData, role: userRole });
  };

  const logout = () => {
    clearAuth();
    setIsAuthenticated(false);
    setRole(null);
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, role, isAuthenticated, loading, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
