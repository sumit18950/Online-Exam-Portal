# Quick Start Guide

## 🚀 Get Running in 3 Steps

### Step 1: Start Backend (if not already running)
```bash
cd online-exam-portal  # Server directory
mvn spring-boot:run
```
Backend will run on: `http://localhost:8082`

### Step 2: Start Frontend
```bash
cd client
npm start
```
Frontend will open at: `http://localhost:3000`

### Step 3: Register & Login
1. Click "Register" at the top
2. Create a new account
3. Login with your credentials
4. You'll be redirected to your dashboard based on your role

---

## 📍 Testing Different Roles

### 👨‍💼 Admin User
- **Purpose**: Manage all users
- **Access**: `/admin/users`
- **Features**:
  - View all users in a table
  - Delete users
  - See user roles and emails

### 👨‍🏫 Teacher User
- **Purpose**: Create and manage exam questions
- **Access**: `/teacher/questions`
- **Features**:
  - Create questions with 4 options
  - Set correct answers
  - View all created questions
  - Delete questions

### 👨‍🎓 Student User
- **Purpose**: Attempt exams and view results
- **Access**: `/student/exams`
- **Features**:
  - View available exams
  - Attempt exams with interactive interface
  - Navigate between questions
  - Submit answers
  - View exam results with scores

---

## 🗂️ What's Inside the Client Folder

```
client/
├── public/              # Static files
├── src/
│   ├── components/      # Navbar, ProtectedRoute
│   ├── context/         # Authentication context
│   ├── pages/          # All page components
│   ├── services/       # API service with axios
│   ├── utils/          # Authentication utilities
│   ├── App.js          # Main routing
│   └── App.css         # Global styles
├── package.json        # Dependencies
└── SETUP.md           # Detailed setup guide
```

---

## 📝 Pages & URLs

| Role | Page | URL | Purpose |
|------|------|-----|---------|
| All | Home | `/` | Welcome page |
| Public | Login | `/login` | User login |
| Public | Register | `/register` | User registration |
| All | Profile | `/profile` | View/edit profile |
| Admin | Users | `/admin/users` | Manage users |
| Teacher | Create Q | `/teacher/questions/create` | Add questions |
| Teacher | Manage Q | `/teacher/questions` | View questions |
| Student | Exams | `/student/exams` | View exams |
| Student | Attempt | `/student/exams/:id/attempt` | Take exam |
| Student | Results | `/student/results` | View scores |

---

## 🔐 How Authentication Works

1. **Register** → Enter username, email, password
2. **Login** → Send credentials to backend
3. **Receive Token** → Backend returns JWT token
4. **Store Token** → Saved in browser's localStorage
5. **Auto Attach** → Token sent with every API request
6. **Role-Based Redirect** → Redirected to role dashboard
7. **Protected Routes** → Can't access pages without token
8. **Auto Logout** → Token expires = forced logout

---

## 🎨 UI Features

- ✅ Responsive design (works on mobile, tablet, desktop)
- ✅ Color-coded role badges
- ✅ Loading states while fetching data
- ✅ Error messages and success notifications
- ✅ Confirmation dialogs for delete actions
- ✅ Form validation
- ✅ Smooth animations and transitions

---

## 🛠️ Useful Commands

```bash
# Start development server
npm start

# Build for production
npm run build

# Run tests
npm test

# Install dependencies
npm install package-name
```

---

## 🔧 Configuration

### Change Backend URL
Edit `src/services/api.js`:
```javascript
const api = axios.create({
  baseURL: 'http://localhost:8082', // Change this
});
```

### Change Port
```bash
npm start -- --port 3001
```

---

## 🐛 Common Issues & Fixes

| Issue | Solution |
|-------|----------|
| Blank white page | Check console errors, refresh page |
| 401 Unauthorized | Logout and login again |
| Can't connect to backend | Ensure backend is running on 8082 |
| Token not saving | Check if localStorage is enabled |
| CORS errors | Check backend CORS configuration |
| Port 3000 in use | Use `npm start -- --port 3001` |

---

## 📱 Try These Features

### As a Student:
1. Login or register as a student
2. Go to "View Exams"
3. Click "Attempt Exam"
4. Answer all questions
5. Click "Submit Exam"
6. Go to "My Results" to see score

### As a Teacher:
1. Login as a teacher
2. Go to "Create Question"
3. Add a question with 4 options
4. Select correct answer
5. Click "Create Question"
6. Go to "Manage Questions" to see all questions

### As an Admin:
1. Login as an admin
2. Go to "View Users"
3. See all registered users
4. Delete a user if needed

---

## 🌐 API Endpoints Used

```
POST   /api/auth/login              # Login
POST   /api/auth/register           # Register
GET    /api/users/profile           # Get profile
PUT    /api/users/update-profile    # Update profile
POST   /api/users/change-password   # Change password
GET    /api/users/all               # Get all users (Admin)
DELETE /api/users/{id}              # Delete user (Admin)
POST   /api/questions/add           # Create question (Teacher)
GET    /api/questions               # Get questions (Teacher)
DELETE /api/questions/{id}          # Delete question (Teacher)
GET    /api/exams                   # Get exams (Student)
GET    /api/exams/{id}/questions    # Get exam questions
POST   /api/exams/{id}/submit       # Submit exam (Student)
GET    /api/users/results           # Get results (Student)
```

---

## 💾 Local Storage Usage

The app stores in localStorage:
- `jwt_token` - Your authentication token
- `user_role` - Your role (ADMIN, TEACHER, STUDENT)
- `user_id` - Your user ID

Clear with: `localStorage.clear()` (in browser console)

---

## 🎯 Next Steps

1. ✅ Ensure backend is running
2. ✅ Start frontend with `npm start`
3. ✅ Register a new account
4. ✅ Login and explore your dashboard
5. ✅ Try different features based on role
6. ✅ Check console for any errors

---

## 📞 Need Help?

- Check `SETUP.md` for detailed setup instructions
- Check `FRONTEND_GUIDE.md` for implementation details
- Look at component files for inline code comments
- Check browser console for error messages (F12)
- Verify backend is running and accessible

**Happy Coding! 🚀**
