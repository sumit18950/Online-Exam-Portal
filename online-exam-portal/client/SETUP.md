# Online Exam Portal - React Frontend Setup

## Project Overview

This is a complete React frontend for the Online Exam Portal with:
- JWT authentication
- Role-based access control (Admin, Teacher, Student)
- Responsive design
- Real-time exam interface

## Quick Start

### 1. Start the Development Server

```bash
cd client
npm start
```

The application will open at `http://localhost:3000`

### 2. Backend Configuration

Ensure your Spring Boot backend is running on `http://localhost:8082`

To change the backend URL, edit `src/services/api.js`:
```javascript
const api = axios.create({
  baseURL: 'http://localhost:8082', // Change this if needed
  headers: {
    'Content-Type': 'application/json',
  },
});
```

## File Structure

```
src/
├── components/
│   ├── Navbar.js           # Navigation with role-based links
│   ├── Navbar.css
│   └── ProtectedRoute.js   # Route protection component
├── context/
│   └── AuthContext.js      # Authentication state management
├── pages/
│   ├── auth/
│   │   ├── Login.js
│   │   ├── Register.js
│   │   └── Auth.css
│   ├── user/
│   │   ├── Profile.js
│   │   ├── UpdateProfile.js
│   │   ├── ChangePassword.js
│   │   └── User.css
│   ├── admin/
│   │   ├── ViewAllUsers.js
│   │   └── Admin.css
│   ├── teacher/
│   │   ├── CreateQuestion.js
│   │   ├── ManageQuestions.js
│   │   └── Teacher.css
│   ├── student/
│   │   ├── ViewExams.js
│   │   ├── AttemptExam.js
│   │   ├── ViewResults.js
│   │   └── Student.css
│   ├── Pages.js            # Home, Unauthorized, NotFound
│   └── Pages.css
├── services/
│   └── api.js              # Axios instance with interceptors
├── utils/
│   └── authUtil.js         # Authentication utilities
├── App.js                  # Main app with routing
├── App.css
└── index.js
```

## Route Structure

### Public Routes
- `/` - Home page
- `/login` - Login page
- `/register` - Registration page
- `/unauthorized` - Unauthorized access page
- `*` - 404 Not Found

### Protected User Routes (Authentication required)
- `/profile` - View user profile
- `/update-profile` - Update profile
- `/change-password` - Change password

### Admin Routes (ADMIN role required)
- `/admin/users` - View all users and delete

### Teacher Routes (TEACHER role required)
- `/teacher/questions/create` - Create new question
- `/teacher/questions` - Manage questions

### Student Routes (STUDENT role required)
- `/student/exams` - View available exams
- `/student/exams/:examId/attempt` - Attempt exam
- `/student/results` - View exam results

## Key Features

### 1. JWT Authentication
- Login/Register with username and password
- JWT token stored in localStorage
- Token automatically attached to API requests
- Auto-logout on token expiration (401)

### 2. Role-Based Access Control
- Admin: Manage users
- Teacher: Create and manage exam questions
- Student: View exams and attempt them

### 3. ProtectedRoute Component
```jsx
// Require authentication only
<ProtectedRoute>
  <Profile />
</ProtectedRoute>

// Require authentication + specific role
<ProtectedRoute requiredRole="ADMIN">
  <AdminDashboard />
</ProtectedRoute>
```

### 4. Navbar with Role-Based Menu
- Shows different links based on user role
- Profile and Logout links for authenticated users
- Login and Register links for unauthenticated users

### 5. Responsive Design
- Mobile-friendly UI
- Tablet and desktop optimized
- Smooth animations and transitions

## API Endpoints Used

### Authentication
- `POST /api/auth/login` - Login
- `POST /api/auth/register` - Register

### User
- `GET /api/users/profile` - Get user profile
- `PUT /api/users/update-profile` - Update profile
- `POST /api/users/change-password` - Change password
- `GET /api/users/all` - Get all users (Admin)
- `DELETE /api/users/{id}` - Delete user (Admin)

### Questions
- `POST /api/questions/add` - Create question (Teacher)
- `GET /api/questions` - Get all questions (Teacher)
- `DELETE /api/questions/{id}` - Delete question (Teacher)

### Exams
- `GET /api/exams` - Get all exams (Student)
- `GET /api/exams/{id}/questions` - Get exam questions (Student)
- `POST /api/exams/{id}/submit` - Submit exam (Student)
- `GET /api/users/results` - Get user results (Student)

## Authentication Flow

1. User visits `/login` or `/register`
2. Credentials sent to backend
3. Backend returns JWT token
4. Token stored in localStorage
5. User role extracted from JWT
6. User redirected to dashboard based on role
7. All subsequent API calls include token in Authorization header
8. On 401 error, user is logged out and redirected to login

## Using the Application

### As a Student
1. Register at `/register`
2. Login at `/login`
3. View available exams at `/student/exams`
4. Click "Attempt Exam" to start
5. Answer questions and submit
6. View results at `/student/results`

### As a Teacher
1. Login at `/login`
2. Go to `/teacher/questions/create` to add questions
3. Go to `/teacher/questions` to manage questions

### As an Admin
1. Login at `/login`
2. Go to `/admin/users` to view all users
3. Delete users as needed

## Error Handling

The application handles various error scenarios:
- **401 Unauthorized**: Session expired → redirect to login
- **403 Forbidden**: Insufficient permissions → show unauthorized page
- **404 Not Found**: Resource not found → show error message
- **Network Error**: Display error message to user

## Development Tips

1. **Using Chrome DevTools**: 
   - Open Console to see errors
   - Check Application → LocalStorage to see stored JWT token
   - Use Network tab to monitor API calls

2. **Testing Authentication**:
   - Check localStorage with: `localStorage.getItem('jwt_token')`
   - Check role with: `localStorage.getItem('user_role')`

3. **API Debugging**:
   - All API calls use the axios instance from `src/services/api.js`
   - Interceptors automatically handle token attachment and errors

## Customization

### Change API Base URL
Edit `src/services/api.js`:
```javascript
baseURL: 'http://your-backend-url:port'
```

### Change Color Scheme
Edit the CSS files in `src/components/` and `src/pages/`:
- Primary color: `#667eea` (Blue)
- Secondary color: `#764ba2` (Purple)
- Success color: `#2ecc71` (Green)
- Error color: `#e74c3c` (Red)

### Add New Routes
Add new routes in `src/App.js`:
```jsx
<Route
  path="/new-page"
  element={
    <ProtectedRoute>
      <NewPage />
    </ProtectedRoute>
  }
/>
```

## Build for Production

```bash
npm run build
```

This creates an optimized production build in the `build/` folder.

## Troubleshooting

### White Blank Page
- Check browser console for errors
- Verify backend is running on the correct port
- Check that all imports are correct

### 401 Unauthorized Errors
- Token may be expired, try logging out and logging in again
- Check that backend is running and JWT secret is correct

### CORS Errors
- Ensure backend has CORS enabled for `http://localhost:3000`
- Check backend security configuration

### Pages Not Loading
- Verify route path matches in `App.js`
- Check that component imports are correct
- Look for typos in file names

## Additional Resources

- [React Documentation](https://reactjs.org/)
- [React Router Documentation](https://reactrouter.com/)
- [Axios Documentation](https://axios-http.com/)
- [JWT Decode Documentation](https://github.com/auth0/jwt-decode)
