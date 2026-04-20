import { jwtDecode } from 'jwt-decode';

export const decodeToken = (token) => {
  try {
    return jwtDecode(token);
  } catch (error) {
    console.error('Error decoding token:', error);
    return null;
  }
};

export const storeToken = (token) => {
  localStorage.setItem('jwt_token', token);
  const decoded = decodeToken(token);
  if (decoded) {
    localStorage.setItem('user_email', decoded.sub || '');
  }
  return decoded;
};

export const storeUserData = (user) => {
  if (user) {
    const role = extractRole(user.role);
    localStorage.setItem('user_role', role);
    localStorage.setItem('user_id', user.id || '');
    localStorage.setItem('user_name', user.username || '');
  }
};

export const extractRole = (roleValue) => {
  if (!roleValue) return '';
  if (typeof roleValue === 'object') {
    const raw = roleValue.roleName || roleValue.name || '';
    return normalizeRole(raw);
  }
  return normalizeRole(String(roleValue));
};

const normalizeRole = (raw) => {
  const upper = raw.trim().toUpperCase();
  if (!upper || upper === '[OBJECT OBJECT]') return '';
  return upper.startsWith('ROLE_') ? upper.substring(5) : upper;
};

export const getToken = () => localStorage.getItem('jwt_token');
export const getRole = () => localStorage.getItem('user_role');
export const getUserId = () => localStorage.getItem('user_id');
export const getUserName = () => localStorage.getItem('user_name');

export const clearAuth = () => {
  localStorage.removeItem('jwt_token');
  localStorage.removeItem('user_role');
  localStorage.removeItem('user_id');
  localStorage.removeItem('user_name');
  localStorage.removeItem('user_email');
};

export const isAuthenticated = () => !!getToken();
export const hasRole = (role) => getRole() === role;
export const hasAnyRole = (roles) => roles.includes(getRole());
