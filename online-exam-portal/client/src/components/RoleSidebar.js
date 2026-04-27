import { NavLink } from 'react-router-dom';

const roleMenus = {
  ADMIN: [
    { label: 'Dashboard', to: '/admin-dashboard' },
    { label: 'Users', to: '/admin/users' },
    { label: 'Exams', to: '/admin/exams' },
    { label: 'Questions', to: '/admin/questions' },
    { label: 'Results', to: '/admin/results' },
  ],
  TEACHER: [
    { label: 'Dashboard', to: '/teacher-dashboard' },
    { label: 'Subjects', to: '/teacher/subjects' },
    { label: 'Exams', to: '/teacher/exams' },
    { label: 'Questions', to: '/teacher/questions' },
    { label: 'Results', to: '/teacher/results' },
  ],
  STUDENT: [
    { label: 'Dashboard', to: '/student-dashboard' },
    { label: 'Exams', to: '/student/exams' },
    { label: 'Results', to: '/student/results' },
  ],
};

export const RoleSidebar = ({ role }) => {
  const menu = roleMenus[role] || [];

  return (
    <aside className="role-sidebar">
      <p className="role-sidebar-title">Workspace</p>
      <nav>
        {menu.map((item) => (
          <NavLink
            key={item.to}
            to={item.to}
            className={({ isActive }) => `role-sidebar-link ${isActive ? 'active' : ''}`}
          >
            {item.label}
          </NavLink>
        ))}
      </nav>
      <div className="role-sidebar-footer">
        <NavLink to="/profile" className="role-sidebar-link">Profile</NavLink>
        <NavLink to="/change-password" className="role-sidebar-link">Password</NavLink>
      </div>
    </aside>
  );
};

