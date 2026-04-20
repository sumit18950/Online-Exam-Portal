# 🎓 Online Exam Portal - Complete React Frontend

A modern, responsive React frontend for the Online Exam Portal with JWT authentication, role-based access control, and a comprehensive exam interface.

## ✨ Features

- **🔐 Secure Authentication**
  - JWT-based login and registration
  - Secure token storage and management
  - Auto-logout on token expiration

- **👥 Role-Based Access Control**
  - Admin: User management
  - Teacher: Question and exam management
  - Student: Exam attempts and result tracking

- **📱 Responsive Design**
  - Mobile-friendly interface
  - Tablet and desktop optimized
  - Smooth animations and transitions

- **📝 Complete Exam Interface**
  - Interactive exam attempts
  - Real-time question navigation
  - Answer tracking and progress indication
  - Instant result display

- **👤 User Management**
  - Profile viewing and editing
  - Password management
  - User preferences

## 🚀 Quick Start

### Prerequisites
- Node.js (v14+)
- npm (v6+)
- Backend running on `http://localhost:8082`

### Installation & Running

1. **Install dependencies**
   ```bash
   cd client
   npm install
   ```

2. **Start development server**
   ```bash
   npm start
   ```

3. **Open in browser**
   ```
   http://localhost:3000
   ```

## 📂 Project Structure

```
client/
├── public/                      # Static assets
├── src/
│   ├── components/              # Reusable components
│   │   ├── Navbar.js           # Navigation bar
│   │   ├── Navbar.css
│   │   ├── ProtectedRoute.js   # Route protection
│   │   └── ProtectedRoute.css
│   │
│   ├── context/                 # React Context
│   │   └── AuthContext.js       # Auth state management
│   │
│   ├── pages/                   # Page components
│   │   ├── auth/               # Authentication pages
│   │   │   ├── Login.js
│   │   │   ├── Register.js
│   │   │   └── Auth.css
│   │   │
│   │   ├── user/               # User pages
│   │   │   ├── Profile.js
│   │   │   ├── UpdateProfile.js
│   │   │   ├── ChangePassword.js
│   │   │   └── User.css
│   │   │
│   │   ├── admin/              # Admin pages
│   │   │   ├── ViewAllUsers.js
│   │   │   └── Admin.css
│   │   │
│   │   ├── teacher/            # Teacher pages
│   │   │   ├── CreateQuestion.js
│   │   │   ├── ManageQuestions.js
│   │   │   └── Teacher.css
│   │   │
│   │   ├── student/            # Student pages
│   │   │   ├── ViewExams.js
│   │   │   ├── AttemptExam.js
│   │   │   ├── ViewResults.js
│   │   │   └── Student.css
│   │   │
│   │   ├── Pages.js            # Static pages
│   │   └── Pages.css
│   │
│   ├── services/                # API services
│   │   └── api.js              # Axios configuration
│   │
│   ├── utils/                   # Utility functions
│   │   └── authUtil.js         # Auth utilities
│   │
│   ├── App.js                  # Main app component
│   ├── App.css                 # Global styles
│   ├── index.js                # Entry point
│   └── index.css
│
├── package.json                # Dependencies
├── SETUP.md                    # Detailed setup guide
└── README.md                   # This file
```

## 🔗 Routes

### Public Routes
- `/` - Home page
- `/login` - User login
- `/register` - User registration
- `/unauthorized` - Access denied
- `/*` - 404 Not found

### Protected Routes (Authentication required)

#### User Routes
- `/profile` - View profile
- `/update-profile` - Edit profile
- `/change-password` - Change password

#### Admin Routes (Admin only)
- `/admin/users` - View and manage users

#### Teacher Routes (Teacher only)
- `/teacher/questions/create` - Create question
- `/teacher/questions` - Manage questions

#### Student Routes (Student only)
- `/student/exams` - View available exams
- `/student/exams/:examId/attempt` - Attempt exam
- `/student/results` - View exam results

## 🔐 Authentication Flow

1. User registers or logs in
2. Credentials sent to backend
3. Backend validates and returns JWT token
4. Frontend stores token in localStorage
5. Token automatically attached to API requests
6. User role extracted from token
7. Role-based redirect to dashboard
8. On token expiration, auto-logout and redirect

## 🎯 Component Documentation

### ProtectedRoute
Protects routes from unauthorized access:
```jsx
<ProtectedRoute>
  <ProtectedPage />
</ProtectedRoute>

<ProtectedRoute requiredRole="ADMIN">
  <AdminPage />
</ProtectedRoute>
```

### Navbar
Displays navigation based on authentication and role:
- Shows different links for each role
- Profile and logout for authenticated users
- Login and register for public users

### AuthContext
Global authentication state:
- `user` - Current user info
- `role` - User role
- `isAuthenticated` - Auth status
- `loading` - Loading state
- `login(token, userData)` - Login method
- `logout()` - Logout method

## 🔌 API Integration

All API requests use axios instance with:
- Automatic JWT token attachment
- Request/response interceptors
- Error handling (401, 403, 404)
- Base URL: `http://localhost:8082`

### Available Endpoints

**Authentication**
- `POST /api/auth/login`
- `POST /api/auth/register`

**User Management**
- `GET /api/users/profile`
- `PUT /api/users/update-profile`
- `POST /api/users/change-password`
- `GET /api/users/all` (Admin)
- `DELETE /api/users/{id}` (Admin)

**Questions**
- `POST /api/questions/add` (Teacher)
- `GET /api/questions` (Teacher)
- `DELETE /api/questions/{id}` (Teacher)

**Exams**
- `GET /api/exams` (Student)
- `GET /api/exams/{id}/questions` (Student)
- `POST /api/exams/{id}/submit` (Student)
- `GET /api/users/results` (Student)

## 🎨 Styling

- **Primary Color**: `#667eea` (Blue)
- **Secondary Color**: `#764ba2` (Purple)
- **Success Color**: `#2ecc71` (Green)
- **Error Color**: `#e74c3c` (Red)
- **Warning Color**: `#f39c12` (Orange)

### Role Badge Colors
- **Admin**: Red (`#e74c3c`)
- **Teacher**: Blue (`#3498db`)
- **Student**: Green (`#2ecc71`)

## 📱 Responsive Breakpoints

- **Mobile**: < 768px
- **Tablet**: 768px - 1199px
- **Desktop**: 1200px+

## 📚 Technologies Used

- **React** - UI library
- **React Router v6** - Client-side routing
- **Axios** - HTTP client
- **jwt-decode** - JWT token decoding
- **CSS3** - Styling
- **Create React App** - Build tool

## 🛠️ Development

### Available Scripts

```bash
# Start development server (port 3000)
npm start

# Build for production
npm run build

# Run tests
npm test

# Eject configuration (irreversible)
npm run eject
```

### Environment Setup

1. **Backend URL**: Configure in `src/services/api.js`
2. **Port**: Modify in package.json or use `npm start -- --port 3001`
3. **JWT Storage**: Configured in localStorage

## 🔒 Security Features

✅ JWT token-based authentication  
✅ Role-based access control  
✅ Protected routes with authentication check  
✅ Automatic 401 error handling  
✅ Secure token storage in localStorage  
✅ Authorization header for API requests  
✅ Form validation and error handling  

## 📋 File Descriptions

### Core Files
- **App.js**: Main component with routing configuration
- **App.css**: Global styles and responsive breakpoints
- **index.js**: Application entry point

### Context
- **AuthContext.js**: Authentication state management with login/logout

### Components
- **Navbar.js**: Navigation bar with role-based links
- **ProtectedRoute.js**: Higher-order component for route protection

### Services
- **api.js**: Axios instance with interceptors for JWT token management

### Utils
- **authUtil.js**: Helper functions for token and role management

### Pages
- **Login/Register**: Authentication pages
- **Profile**: User profile management
- **Admin**: User management interface
- **Teacher**: Question management interface
- **Student**: Exam interface with result tracking

## 🐛 Troubleshooting

### Common Issues

**White blank page**
- Check browser console for errors
- Verify backend is accessible
- Refresh page and clear cache

**401 Unauthorized**
- Token may have expired
- Try logging out and logging back in
- Check JWT format in backend

**Cannot connect to backend**
- Ensure backend is running on port 8082
- Check CORS configuration in backend
- Update base URL in `src/services/api.js`

**Port 3000 already in use**
```bash
npm start -- --port 3001
```

**localStorage issues**
- Check if localStorage is enabled in browser
- Clear localStorage: `localStorage.clear()` in console

## 📖 Documentation Files

- **QUICKSTART.md** - Quick start guide with examples
- **SETUP.md** - Detailed setup and configuration
- **FRONTEND_GUIDE.md** - Complete implementation guide
- **TESTING_CHECKLIST.md** - Comprehensive testing checklist

## 🤝 Contributing

When adding new features:
1. Create components in appropriate directories
2. Follow existing code structure
3. Add corresponding CSS files
4. Update routing in App.js
5. Test on different screen sizes
6. Ensure API integration works

## 📄 License

This project is part of the Online Exam Portal system.

## 🎓 Educational Use

This project is designed for educational purposes and demonstrates:
- React functional components and hooks
- React Router for SPA navigation
- JWT authentication and authorization
- API integration with Axios
- Context API for state management
- Responsive CSS design
- Form handling and validation
- Error handling and user feedback

## 🚀 Deployment

To deploy for production:

1. Build the application
   ```bash
   npm run build
   ```

2. Update backend URL for production

3. Deploy the `build/` folder to:
   - Vercel
   - Netlify
   - AWS S3 + CloudFront
   - Any static hosting service

## 📞 Support

For issues or questions:
1. Check the documentation files
2. Review browser console for errors
3. Verify backend connectivity
4. Check component implementations
5. Refer to code comments

---

## ✅ What's Included

✅ Complete authentication system  
✅ Role-based dashboards  
✅ User profile management  
✅ Admin user management  
✅ Teacher question management  
✅ Student exam interface  
✅ Real-time results display  
✅ Responsive design  
✅ Error handling  
✅ Comprehensive documentation  

## 🎯 Ready to Use

Everything is configured and ready to run:
- No additional setup required
- All dependencies installed
- All components implemented
- All routes configured
- All styles included

**Start the server and begin using the application!**

---

**Built with ❤️ for Online Exam Portal**
