import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import { Navbar } from './components/Navbar';
import { ProtectedRoute } from './components/ProtectedRoute';

import { Login } from './pages/auth/Login';
import { Register } from './pages/auth/Register';

import { Profile } from './pages/user/Profile';
import { UpdateProfile } from './pages/user/UpdateProfile';
import { ChangePassword } from './pages/user/ChangePassword';

import { AdminDashboard } from './pages/admin/AdminDashboard';
import { ViewAllUsers } from './pages/admin/ViewAllUsers';
import { CreateUser } from './pages/admin/CreateUser';
import { AdminManageSubjects } from './pages/admin/AdminManageSubjects';
import { AdminManageExams } from './pages/admin/AdminManageExams';
import { AdminManageQuestions } from './pages/admin/AdminManageQuestions';
import { AdminCreateQuestion } from './pages/admin/AdminCreateQuestion';
import { AdminResults } from './pages/admin/AdminResults';

import { TeacherDashboard } from './pages/teacher/TeacherDashboard';
import { ManageSubjects } from './pages/teacher/ManageSubjects';
import { CreateQuestion } from './pages/teacher/CreateQuestion';
import { ManageQuestions } from './pages/teacher/ManageQuestions';
import { TeacherResults } from './pages/teacher/TeacherResults';
import { ManageExams } from './pages/teacher/ManageExams';

import { StudentDashboard } from './pages/student/StudentDashboard';
import { ViewExams } from './pages/student/ViewExams';
import { AttemptExam } from './pages/student/AttemptExam';
import { ViewResults } from './pages/student/ViewResults';

import { Home, Unauthorized, NotFound } from './pages/Pages';

import './components/ui/UI.css';
import './components/RoleSidebar.css';
import './App.css';

function App() {
  return (
    <Router>
      <AuthProvider>
        <Navbar />
        <Routes>
          {/* Public Routes */}
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/unauthorized" element={<Unauthorized />} />

          {/* Common Protected Routes */}
          <Route path="/profile" element={<ProtectedRoute><Profile /></ProtectedRoute>} />
          <Route path="/update-profile" element={<ProtectedRoute><UpdateProfile /></ProtectedRoute>} />
          <Route path="/change-password" element={<ProtectedRoute><ChangePassword /></ProtectedRoute>} />

          {/* Admin Routes */}
          <Route path="/admin-dashboard" element={<ProtectedRoute requiredRole="ADMIN"><AdminDashboard /></ProtectedRoute>} />
          <Route path="/admin/users" element={<ProtectedRoute requiredRole="ADMIN"><ViewAllUsers /></ProtectedRoute>} />
          <Route path="/admin/create-user" element={<ProtectedRoute requiredRole="ADMIN"><CreateUser /></ProtectedRoute>} />
          <Route path="/admin/subjects" element={<ProtectedRoute requiredRole="ADMIN"><AdminManageSubjects /></ProtectedRoute>} />
          <Route path="/admin/exams" element={<ProtectedRoute requiredRole="ADMIN"><AdminManageExams /></ProtectedRoute>} />
          <Route path="/admin/questions" element={<ProtectedRoute requiredRole="ADMIN"><AdminManageQuestions /></ProtectedRoute>} />
          <Route path="/admin/questions/create" element={<ProtectedRoute requiredRole="ADMIN"><AdminCreateQuestion /></ProtectedRoute>} />
          <Route path="/admin/results" element={<ProtectedRoute requiredRole="ADMIN"><AdminResults /></ProtectedRoute>} />

          {/* Teacher Routes */}
          <Route path="/teacher-dashboard" element={<ProtectedRoute requiredRole="TEACHER"><TeacherDashboard /></ProtectedRoute>} />
          <Route path="/teacher/subjects" element={<ProtectedRoute requiredRole="TEACHER"><ManageSubjects /></ProtectedRoute>} />
          <Route path="/teacher/exams" element={<ProtectedRoute requiredRole="TEACHER"><ManageExams /></ProtectedRoute>} />
          <Route path="/teacher/questions" element={<ProtectedRoute requiredRole="TEACHER"><ManageQuestions /></ProtectedRoute>} />
          <Route path="/teacher/questions/create" element={<ProtectedRoute requiredRole="TEACHER"><CreateQuestion /></ProtectedRoute>} />
          <Route path="/teacher/results" element={<ProtectedRoute requiredRole="TEACHER"><TeacherResults /></ProtectedRoute>} />

          {/* Student Routes */}
          <Route path="/student-dashboard" element={<ProtectedRoute requiredRole="STUDENT"><StudentDashboard /></ProtectedRoute>} />
          <Route path="/student/exams" element={<ProtectedRoute requiredRole="STUDENT"><ViewExams /></ProtectedRoute>} />
          <Route path="/student/exams/:examId/attempt" element={<ProtectedRoute requiredRole="STUDENT"><AttemptExam /></ProtectedRoute>} />
          <Route path="/student/results" element={<ProtectedRoute requiredRole="STUDENT"><ViewResults /></ProtectedRoute>} />

          {/* Fallback */}
          <Route path="*" element={<NotFound />} />
        </Routes>
      </AuthProvider>
    </Router>
  );
}

export default App;
