# 🔍 Developer Quick Reference Guide

## Quick Commands

```bash
# Start development
npm start

# Build for production
npm run build

# Install new package
npm install package-name

# Build watch mode
npm run build -- --watch
```

## Key Files & Their Purpose

| File | Purpose |
|------|---------|
| `App.js` | Main routing configuration |
| `AuthContext.js` | Global auth state |
| `api.js` | Axios instance with interceptors |
| `authUtil.js` | Auth helper functions |
| `Navbar.js` | Navigation component |
| `ProtectedRoute.js` | Route protection |

## Common Import Patterns

```javascript
// API calls
import api from '../services/api';

// Context
import { AuthContext } from '../context/AuthContext';
import { useContext } from 'react';

// Navigation
import { useNavigate, useParams } from 'react-router-dom';

// Auth utilities
import { storeToken, getRole, isAuthenticated } from '../utils/authUtil';
```

## API Call Example

```javascript
// GET request
const response = await api.get('/api/users/profile');
const data = response.data;

// POST request
await api.post('/api/auth/login', {
  username: 'john',
  password: 'pass123'
});

// PUT request
await api.put('/api/users/update-profile', {
  firstName: 'John'
});

// DELETE request
await api.delete(`/api/users/${userId}`);
```

## Component Structure Template

```javascript
import React, { useState, useEffect, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';
import { AuthContext } from '../context/AuthContext';
import './ComponentName.css';

export const ComponentName = () => {
  // State
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  
  // Hooks
  const navigate = useNavigate();
  const { role } = useContext(AuthContext);
  
  // Effects
  useEffect(() => {
    fetchData();
  }, []);
  
  // Methods
  const fetchData = async () => {
    try {
      const response = await api.get('/api/endpoint');
      setData(response.data);
      setError('');
    } catch (err) {
      setError(err.response?.data?.message || 'Error');
    } finally {
      setLoading(false);
    }
  };
  
  // Render
  if (loading) return <div>Loading...</div>;
  if (error) return <div>{error}</div>;
  
  return <div>{/* JSX */}</div>;
};
```

## Protected Route Pattern

```javascript
<Route
  path="/admin/users"
  element={
    <ProtectedRoute requiredRole="ADMIN">
      <ViewAllUsers />
    </ProtectedRoute>
  }
/>
```

## Form Handling Pattern

```javascript
const [formData, setFormData] = useState({
  username: '',
  email: '',
});

const handleChange = (e) => {
  const { name, value } = e.target;
  setFormData(prev => ({
    ...prev,
    [name]: value,
  }));
};

const handleSubmit = async (e) => {
  e.preventDefault();
  try {
    await api.post('/api/endpoint', formData);
    // Success
  } catch (err) {
    // Error
  }
};
```

## Conditional Rendering

```javascript
// Loading
{loading && <div>Loading...</div>}

// Empty state
{data.length === 0 && <p>No data</p>}

// Error
{error && <div className="error">{error}</div>}

// Success
{success && <div className="success">{success}</div>}

// Role-based
{role === 'ADMIN' && <AdminFeature />}
```

## CSS Class Naming

```css
/* Block */
.container { }

/* Element */
.card-header { }

/* Modifier */
.btn-primary { }
.btn-secondary { }
.role-admin { }

/* State */
.loading { }
.error { }
.success { }
```

## Common Patterns

### Fetch Data on Mount
```javascript
useEffect(() => {
  const fetchData = async () => {
    try {
      const response = await api.get('/api/data');
      setData(response.data);
    } catch (err) {
      setError(err.response?.data?.message);
    } finally {
      setLoading(false);
    }
  };
  
  fetchData();
}, []);
```

### Navigation on Action
```javascript
const navigate = useNavigate();

const handleSuccess = () => {
  navigate('/next-page');
};
```

### Context Usage
```javascript
const { user, role, logout } = useContext(AuthContext);

const handleLogout = () => {
  logout();
  navigate('/login');
};
```

### Form Submission
```javascript
const handleSubmit = async (e) => {
  e.preventDefault();
  setSubmitting(true);
  
  try {
    await api.post('/api/endpoint', formData);
    setSuccess('Success!');
    setTimeout(() => navigate('/page'), 2000);
  } catch (err) {
    setError(err.response?.data?.message);
  } finally {
    setSubmitting(false);
  }
};
```

## URL Parameters

```javascript
// From route
import { useParams } from 'react-router-dom';
const { examId } = useParams();

// Use in API call
const response = await api.get(`/api/exams/${examId}/questions`);
```

## Query Parameters (if needed)

```javascript
// From URL
import { useLocation } from 'react-router-dom';
const location = useLocation();
const params = new URLSearchParams(location.search);

// Set with navigate
navigate(`/page?id=123`);
```

## LocalStorage Usage

```javascript
// Store
localStorage.setItem('jwt_token', token);

// Retrieve
const token = localStorage.getItem('jwt_token');

// Remove
localStorage.removeItem('jwt_token');

// Clear all
localStorage.clear();
```

## Debugging

```javascript
// Console logging
console.log('value:', value);

// Check token
console.log(localStorage.getItem('jwt_token'));

// Check role
console.log(localStorage.getItem('user_role'));

// Check state
console.log('data:', data);
```

## Common Error Messages

| Error | Cause | Solution |
|-------|-------|----------|
| 401 | Token expired | Logout and login again |
| 403 | Wrong role | Access denied, not authorized |
| 404 | Endpoint not found | Check API URL |
| 500 | Server error | Check backend logs |
| CORS | Cross-origin | Check backend CORS config |

## Performance Tips

- Use `useCallback` for event handlers
- Use `useMemo` for expensive computations
- Lazy load routes with `React.lazy()`
- Optimize re-renders
- Minimize bundle size
- Cache API responses

## Component Best Practices

✅ Use functional components  
✅ Use hooks (useState, useEffect, useContext)  
✅ Keep components small and focused  
✅ Use proper prop types  
✅ Add error handling  
✅ Add loading states  
✅ Use semantic HTML  
✅ Keep CSS organized  

## Security Checklist

✅ Never store sensitive data in code  
✅ Always validate forms  
✅ Use HTTPS in production  
✅ Keep dependencies updated  
✅ Don't expose API keys  
✅ Validate user input  
✅ Use secure authentication  
✅ Implement CSRF protection  

## File Structure Checklist

When adding new pages:
```
pages/category/
├── NewPage.js          # Component
├── NewPage.css         # Styles
└── index.js            # Export (optional)
```

When adding new components:
```
components/
├── NewComponent.js     # Component
└── NewComponent.css    # Styles
```

## Routes Checklist

✅ Public routes `/login`, `/register`  
✅ Protected routes need `<ProtectedRoute>`  
✅ Role-specific routes need `requiredRole`  
✅ Redirect after login based on role  
✅ 404 fallback for unknown routes  
✅ Unauthorized page for access denied  

## Testing Checklist

Before deploying:
- [ ] All pages load
- [ ] All buttons work
- [ ] All forms submit
- [ ] All API calls work
- [ ] All errors show properly
- [ ] Responsive on mobile
- [ ] Responsive on tablet
- [ ] Responsive on desktop
- [ ] No console errors
- [ ] No console warnings

## Git Workflow

```bash
# Check status
git status

# Add changes
git add .

# Commit
git commit -m "Feature description"

# Push
git push origin branch-name
```

## Deployment Checklist

- [ ] Build succeeds: `npm run build`
- [ ] All tests pass
- [ ] No console errors
- [ ] No console warnings
- [ ] Environment variables set
- [ ] API URL updated for production
- [ ] Security headers added
- [ ] HTTPS enabled
- [ ] Cache busting configured
- [ ] Monitoring enabled

## Quick Troubleshooting

| Issue | Check |
|-------|-------|
| Page won't load | Check console, verify route |
| API not working | Check backend, verify URL, token |
| CSS not working | Check file import, refresh cache |
| Token not saving | Check localStorage enabled |
| Navigation not working | Check route path, component export |

## Useful Extensions

- React Developer Tools
- Redux DevTools
- Axios devtools
- ES7+ React/Redux snippets

## Resources

- [React Docs](https://react.dev)
- [React Router Docs](https://reactrouter.com)
- [Axios Docs](https://axios-http.com)
- [MDN Web Docs](https://developer.mozilla.org)
- [CSS-Tricks](https://css-tricks.com)

---

**Keep this guide handy for quick reference while developing!**
