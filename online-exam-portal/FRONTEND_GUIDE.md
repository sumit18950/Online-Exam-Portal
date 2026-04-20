# Online Exam Portal - Frontend Implementation Guide

## Summary of Implemented Components

### 1. Authentication System
- **Login Page** (`/login`)
  - Username and password fields
  - Error handling and loading states
  - Redirects to role-specific dashboard on success
  
- **Register Page** (`/register`)
  - Username, email, password, and confirm password fields
  - Password validation
  - Redirects to login on success

### 2. User Management
- **Profile Page** (`/profile`)
  - View current user information
  - Links to update profile and change password

- **Update Profile Page** (`/update-profile`)
  - Update first name, last name, and email
  - Success/error notifications
  
- **Change Password Page** (`/change-password`)
  - Old password verification
  - New password confirmation
  - Success feedback

### 3. Admin Dashboard
- **View All Users** (`/admin/users`)
  - Table displaying all users with ID, username, email, and role
  - Role badges with color coding
  - Delete user functionality with confirmation

### 4. Teacher Dashboard
- **Create Question** (`/teacher/questions/create`)
  - Form to add new questions with 4 options
  - Correct answer selection
  - Exam ID input
  
- **Manage Questions** (`/teacher/questions`)
  - Display all created questions
  - Edit option indicators (soon)
  - Delete question functionality
  - Quick add new question button

### 5. Student Dashboard
- **View Exams** (`/student/exams`)
  - Grid layout of available exams
  - Exam details: duration, number of questions, max marks
  - "Attempt Exam" button for each exam
  
- **Attempt Exam** (`/student/exams/:examId/attempt`)
  - Interactive exam interface
  - Questions displayed one at a time
  - Radio buttons for answer selection
  - Previous/Next navigation
  - Question navigator showing answered/unanswered
  - Submit button at the end
  
- **View Results** (`/student/results`)
  - Grid of all exam results
  - Score, total marks, percentage, and status
  - Color-coded performance indicators
  - Result submission date

### 6. Navigation & Routing
- **Navbar Component** (`src/components/Navbar.js`)
  - Logo and brand name
  - Role-based navigation links
  - Logout button for authenticated users
  - Responsive mobile menu

- **ProtectedRoute Component** (`src/components/ProtectedRoute.js`)
  - Authentication check
  - Role-based access control
  - Loading state during auth check
  - Redirects to login or unauthorized page

- **AuthContext** (`src/context/AuthContext.js`)
  - Global authentication state
  - User and role information
  - Login/logout methods

### 7. Services & Utilities
- **API Service** (`src/services/api.js`)
  - Axios instance with base URL
  - Request interceptor: adds JWT token to headers
  - Response interceptor: handles 401 errors
  - Auto-logout and redirect on token expiration
  
- **Auth Utilities** (`src/utils/authUtil.js`)
  - `decodeToken()` - Decode JWT
  - `storeToken()` - Store token and extract role
  - `getToken()` - Retrieve stored token
  - `getRole()` - Get user role
  - `clearAuth()` - Clear authentication data
  - `isAuthenticated()` - Check auth status
  - `hasRole()` - Check specific role
  - `hasAnyRole()` - Check multiple roles

### 8. Static Pages
- **Home Page** (`/`)
  - Welcome message
  - Feature list
  - Portal overview

- **Unauthorized Page** (`/unauthorized`)
  - Error message for access denied
  - Link back to home

- **Not Found Page** (`*`)
  - 404 error page
  - Link back to home

## Technology Stack

- **Frontend**: React 18
- **Routing**: React Router v6
- **HTTP Client**: Axios
- **JWT Decoding**: jwt-decode
- **Styling**: CSS3
- **Build Tool**: Create React App

## File Organization

```
client/
├── src/
│   ├── components/
│   │   ├── Navbar.js
│   │   ├── Navbar.css
│   │   └── ProtectedRoute.js
│   ├── context/
│   │   └── AuthContext.js
│   ├── pages/
│   │   ├── auth/
│   │   │   ├── Login.js
│   │   │   ├── Register.js
│   │   │   └── Auth.css
│   │   ├── user/
│   │   │   ├── Profile.js
│   │   │   ├── UpdateProfile.js
│   │   │   ├── ChangePassword.js
│   │   │   └── User.css
│   │   ├── admin/
│   │   │   ├── ViewAllUsers.js
│   │   │   └── Admin.css
│   │   ├── teacher/
│   │   │   ├── CreateQuestion.js
│   │   │   ├── ManageQuestions.js
│   │   │   └── Teacher.css
│   │   ├── student/
│   │   │   ├── ViewExams.js
│   │   │   ├── AttemptExam.js
│   │   │   ├── ViewResults.js
│   │   │   └── Student.css
│   │   ├── Pages.js
│   │   └── Pages.css
│   ├── services/
│   │   └── api.js
│   ├── utils/
│   │   └── authUtil.js
│   ├── App.js
│   ├── App.css
│   └── index.js
├── public/
├── package.json
└── SETUP.md
```

## Key Features Implemented

### Authentication & Authorization
✅ JWT token storage in localStorage
✅ Automatic token attachment to API requests
✅ Role extraction from JWT
✅ Auto-logout on token expiration
✅ Role-based route protection
✅ Redirect on unauthorized access

### User Experience
✅ Loading states for async operations
✅ Error messages with user feedback
✅ Success notifications
✅ Confirmation dialogs for destructive actions
✅ Responsive design for mobile/tablet/desktop
✅ Form validation

### API Integration
✅ GET requests for fetching data
✅ POST requests for creating data
✅ PUT requests for updating data
✅ DELETE requests for removing data
✅ Error handling (401, 403, 404, network errors)
✅ Request/response interceptors

### UI/UX
✅ Navbar with role-based navigation
✅ Color-coded badges for roles and status
✅ Consistent styling across pages
✅ Smooth transitions and hover effects
✅ Responsive layout
✅ Clear visual hierarchy

## Usage Instructions

### Running the Application

1. **Start Backend**
   ```bash
   # In the server directory
   mvn spring-boot:run
   ```

2. **Start Frontend**
   ```bash
   # In the client directory
   npm start
   ```

3. **Access the Application**
   - Open `http://localhost:3000` in your browser

### Testing Different Roles

1. **Register and Login**
   - Register a new account
   - Login with credentials
   - System will redirect based on role

2. **Test Admin Features**
   - Login as admin user
   - Access `/admin/users` to view/delete users

3. **Test Teacher Features**
   - Login as teacher user
   - Create questions at `/teacher/questions/create`
   - Manage questions at `/teacher/questions`

4. **Test Student Features**
   - Login as student user
   - View exams at `/student/exams`
   - Attempt exam and submit answers
   - View results at `/student/results`

## API Endpoints Reference

### Authentication
```
POST /api/auth/login
POST /api/auth/register
```

### User Management
```
GET /api/users/profile
PUT /api/users/update-profile
POST /api/users/change-password
GET /api/users/all (Admin only)
DELETE /api/users/{id} (Admin only)
```

### Questions
```
POST /api/questions/add (Teacher only)
GET /api/questions (Teacher only)
DELETE /api/questions/{id} (Teacher only)
```

### Exams
```
GET /api/exams (Student only)
GET /api/exams/{id}/questions (Student only)
POST /api/exams/{id}/submit (Student only)
GET /api/users/results (Student only)
```

## Styling & Colors

**Primary Colors**
- Blue: `#667eea`
- Purple: `#764ba2`
- Dark: `#2c3e50`

**Status Colors**
- Success/Pass: `#2ecc71` (Green)
- Warning/Average: `#f39c12` (Orange)
- Error/Fail: `#e74c3c` (Red)
- Neutral: `#95a5a6` (Gray)

**Role Badge Colors**
- Admin: Red (`#e74c3c`)
- Teacher: Blue (`#3498db`)
- Student: Green (`#2ecc71`)

## Responsive Breakpoints

- **Desktop**: 1200px and above
- **Tablet**: 768px to 1199px
- **Mobile**: Below 768px

## Security Features

✅ JWT token-based authentication
✅ Role-based access control
✅ Protected routes
✅ Secure token storage
✅ Auto-logout on token expiration
✅ Authorization header for API requests
✅ Input validation on forms

## Next Steps / Future Enhancements

1. Add question editing functionality
2. Implement exam timer for attempts
3. Add real-time notifications
4. Implement search and filter functionality
5. Add pagination for large datasets
6. Implement student progress tracking
7. Add analytics and statistics
8. Implement two-factor authentication
9. Add profile picture upload
10. Implement real-time exam rankings

## Troubleshooting

### Port Already in Use
If port 3000 is already in use:
```bash
npm start -- --port 3001
```

### Backend Connection Issues
1. Verify backend is running on port 8082
2. Check CORS configuration in backend
3. Update API base URL in `src/services/api.js`

### Authentication Issues
1. Clear localStorage and try logging in again
2. Check JWT token format in backend
3. Verify role field name in JWT payload

### Styling Issues
1. Clear browser cache (Ctrl+Shift+Delete)
2. Hard refresh page (Ctrl+Shift+R)
3. Check CSS file imports

## Contact & Support

For issues or questions about the frontend implementation, refer to the SETUP.md guide or check the inline code comments.
