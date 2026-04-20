# Testing Checklist for Online Exam Portal Frontend

## Pre-Testing Setup
- [ ] Backend is running on `http://localhost:8082`
- [ ] Frontend dependencies installed (`npm install`)
- [ ] Frontend started (`npm start`)
- [ ] Browser console is open (F12)
- [ ] localStorage is cleared before testing

---

## Authentication Testing

### Register Page (`/register`)
- [ ] Can access register page
- [ ] Form validation works:
  - [ ] Empty fields show error
  - [ ] Passwords must match (validation message appears)
  - [ ] Email format validation works
- [ ] Can register with valid credentials
- [ ] Redirects to login after successful registration
- [ ] Error message shows for duplicate username/email
- [ ] Loading state shows while registering

### Login Page (`/login`)
- [ ] Can access login page
- [ ] Form validation works (empty fields error)
- [ ] Can login with valid credentials
- [ ] Error message shows for invalid credentials
- [ ] Token saved to localStorage after login
- [ ] User role saved to localStorage
- [ ] Loading state shows while logging in
- [ ] Redirects to correct dashboard after login:
  - [ ] Admin → `/admin/users`
  - [ ] Teacher → `/teacher/questions`
  - [ ] Student → `/student/exams`

### Logout
- [ ] Logout button appears in navbar
- [ ] Clicking logout removes token from localStorage
- [ ] Redirects to login page after logout
- [ ] Cannot access protected pages after logout

---

## Navbar Testing

### Navigation Links
- [ ] Logo/Home link works
- [ ] Profile link visible and clickable
- [ ] Logout button visible and clickable
- [ ] Links change based on authentication status
- [ ] Links change based on user role

### Role-Based Navigation
- [ ] Admin sees: View Users link
- [ ] Teacher sees: Create Question, Manage Questions links
- [ ] Student sees: View Exams, My Results links
- [ ] Unauthenticated sees: Login, Register links

---

## Protected Routes Testing

### Route Protection
- [ ] Cannot access protected pages without login
- [ ] Redirects to login when accessing protected page
- [ ] Can access protected pages when logged in

### Role-Based Route Protection
- [ ] Admin cannot access teacher pages
- [ ] Teacher cannot access admin pages
- [ ] Student cannot access admin pages
- [ ] Redirects to unauthorized page for wrong role

---

## User Pages Testing

### Profile Page (`/profile`)
- [ ] Can load profile page
- [ ] Displays user information (username, email, role, name)
- [ ] Loading state shows initially
- [ ] Error message shows if profile fetch fails
- [ ] "Update Profile" button works
- [ ] "Change Password" button works

### Update Profile Page (`/update-profile`)
- [ ] Can load update page
- [ ] Form pre-populated with current profile data
- [ ] Can update first name
- [ ] Can update last name
- [ ] Can update email
- [ ] Success message appears after update
- [ ] Redirects to profile page on success
- [ ] Error message shows on failure
- [ ] Cancel button works

### Change Password Page (`/change-password`)
- [ ] Can load change password page
- [ ] Requires old password
- [ ] New password and confirm password must match
- [ ] Validation error if passwords don't match
- [ ] Success message appears after change
- [ ] Redirects to profile page on success
- [ ] Error message shows for incorrect old password
- [ ] Cancel button works

---

## Admin Pages Testing

### View All Users Page (`/admin/users`)
- [ ] Can load users page (admin role only)
- [ ] Displays table of all users
- [ ] Shows columns: ID, Username, Email, Role, Actions
- [ ] Role badges have correct colors:
  - [ ] Admin: Red
  - [ ] Teacher: Blue
  - [ ] Student: Green
- [ ] Loading state shows initially
- [ ] Delete button appears for each user
- [ ] Confirmation dialog appears on delete
- [ ] User removed from table after deletion
- [ ] Error message shows if delete fails
- [ ] Non-admin users cannot access this page

---

## Teacher Pages Testing

### Create Question Page (`/teacher/questions/create`)
- [ ] Can load create question page (teacher role only)
- [ ] Can enter exam ID
- [ ] Can enter question content (textarea)
- [ ] Can enter 4 options
- [ ] Can select correct option (1-4)
- [ ] Form validation works (required fields)
- [ ] Success message appears after creation
- [ ] Redirects to manage questions page on success
- [ ] Error message shows on failure
- [ ] Cancel button works
- [ ] Non-teacher users cannot access this page

### Manage Questions Page (`/teacher/questions`)
- [ ] Can load manage questions page (teacher role only)
- [ ] Displays all questions in list format
- [ ] Shows question content
- [ ] Shows all 4 options
- [ ] Highlights correct answer
- [ ] "Add New Question" button works
- [ ] Delete button appears for each question
- [ ] Confirmation dialog appears on delete
- [ ] Question removed from list after deletion
- [ ] Empty state message if no questions
- [ ] Loading state shows initially
- [ ] Non-teacher users cannot access this page

---

## Student Pages Testing

### View Exams Page (`/student/exams`)
- [ ] Can load exams page (student role only)
- [ ] Displays grid of available exams
- [ ] Shows exam title and description
- [ ] Shows duration, number of questions, max marks
- [ ] "Attempt Exam" button visible and clickable
- [ ] Clicking button navigates to attempt page
- [ ] Empty state message if no exams
- [ ] Loading state shows initially
- [ ] Non-student users cannot access this page

### Attempt Exam Page (`/student/exams/:examId/attempt`)
- [ ] Can load attempt page for specific exam
- [ ] Shows current question number
- [ ] Displays question content
- [ ] Shows 4 radio button options
- [ ] Can select an option
- [ ] Previous/Next buttons work for navigation
- [ ] Previous button disabled on first question
- [ ] Next button disabled on last question
- [ ] Submit button appears on last question
- [ ] Question navigator shows:
    - [ ] Total number of questions
    - [ ] Answered questions highlighted
    - [ ] Current question highlighted
    - [ ] Can click to jump to question
- [ ] Selected answers are preserved when navigating
- [ ] Answer counter shows "X / Y answered"
- [ ] Confirmation dialog on submit
- [ ] Loading state shows during submit
- [ ] Redirects to results page after submit
- [ ] Cannot submit incomplete exam

### View Results Page (`/student/results`)
- [ ] Can load results page (student role only)
- [ ] Displays grid of all exam results
- [ ] Shows exam title
- [ ] Shows score and total marks
- [ ] Shows percentage
- [ ] Shows status (Pass/Fail/etc)
- [ ] Shows submission date
- [ ] Percentage color-coded:
    - [ ] Green for 75%+
    - [ ] Yellow for 50-74%
    - [ ] Red for below 50%
- [ ] Empty state message if no results
- [ ] Loading state shows initially
- [ ] Non-student users cannot access this page

---

## Error Handling Testing

### 401 Unauthorized
- [ ] Force logout by clearing token in localStorage
- [ ] Try to access protected API
- [ ] Auto-redirects to login
- [ ] Error message shown

### 403 Forbidden
- [ ] Login as student
- [ ] Try to access `/admin/users`
- [ ] Redirects to unauthorized page

### 404 Not Found
- [ ] Navigate to non-existent URL (e.g., `/nonexistent`)
- [ ] Shows 404 page
- [ ] Can navigate back to home

### Network Errors
- [ ] Stop backend server
- [ ] Try to make API call
- [ ] Error message displayed to user

---

## Responsive Design Testing

### Mobile (375px width)
- [ ] Navbar responsive and functional
- [ ] Forms stack vertically
- [ ] Buttons full width
- [ ] Tables have horizontal scroll
- [ ] Grid layouts become single column
- [ ] Text is readable

### Tablet (768px width)
- [ ] Layout adjusts appropriately
- [ ] All features accessible
- [ ] Touch-friendly button sizes

### Desktop (1200px+ width)
- [ ] Full layout displays correctly
- [ ] Grid layouts show multiple columns
- [ ] Tables display properly
- [ ] No horizontal scrolling needed

---

## API Integration Testing

### JWT Token Handling
- [ ] Token sent in Authorization header (Bearer {token})
- [ ] Token persists in localStorage
- [ ] Token used for subsequent API calls
- [ ] Token removed on logout

### Request/Response
- [ ] GET requests fetch data correctly
- [ ] POST requests create data correctly
- [ ] PUT requests update data correctly
- [ ] DELETE requests remove data correctly

### Interceptors
- [ ] Token auto-attached to requests
- [ ] 401 errors trigger logout
- [ ] 401 redirects to login
- [ ] Other errors show messages

---

## Performance Testing

- [ ] Pages load quickly
- [ ] No console errors
- [ ] No console warnings
- [ ] Images load properly
- [ ] Animations are smooth
- [ ] No memory leaks (check DevTools Memory)
- [ ] API calls are efficient

---

## Accessibility Testing

- [ ] Form labels properly associated with inputs
- [ ] Can navigate with Tab key
- [ ] Buttons are keyboard accessible
- [ ] Color contrast is sufficient
- [ ] Error messages are descriptive
- [ ] Loading states are indicated

---

## Browser Testing

- [ ] Works in Chrome
- [ ] Works in Firefox
- [ ] Works in Safari
- [ ] Works in Edge
- [ ] Works in mobile Safari (iOS)
- [ ] Works in Chrome Mobile (Android)

---

## Cross-Browser Data Consistency

- [ ] Data syncs across tabs/windows
- [ ] Logout in one tab affects another tab
- [ ] Token changes reflect everywhere

---

## Session Management

- [ ] Can maintain session across page reloads
- [ ] Logout clears session completely
- [ ] Back button doesn't restore auth
- [ ] Forward button doesn't access deleted data

---

## Edge Cases

- [ ] Enter button submits form
- [ ] Special characters in inputs handled
- [ ] Very long usernames display correctly
- [ ] Empty search results show message
- [ ] Multiple file uploads handled
- [ ] Network timeout handled gracefully

---

## Final Verification Checklist

- [ ] All pages load without errors
- [ ] All buttons are clickable and functional
- [ ] All forms work and validate correctly
- [ ] All API calls succeed
- [ ] Redirects work properly
- [ ] Error handling works
- [ ] Responsive design looks good
- [ ] No console errors or warnings
- [ ] Performance is acceptable
- [ ] Backend integration is seamless

---

## Notes for Testers

- Test with different user roles (Admin, Teacher, Student)
- Test with fresh browser session (clear localStorage and cookies)
- Test with existing session
- Test with various input data (normal, edge cases, special characters)
- Test on different devices/screen sizes
- Test with slow network (use DevTools throttling)
- Report any issues with steps to reproduce

---

## Sign-Off

- [ ] All tests passed
- [ ] No critical issues
- [ ] Ready for production

**Tested by**: _________________  
**Date**: _________________  
**Remarks**: _________________
