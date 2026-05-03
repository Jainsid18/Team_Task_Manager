# Backend Integration Guide - Task Manager

## 🎯 Overview

Your application now has a **complete full-stack setup** with:
- ✅ **Frontend:** Vanilla HTML/CSS/JavaScript (in `/workspace/`)
- ✅ **Backend:** Spring Boot REST API (in `/workspace/inputs/taskmanager/`)
- ✅ **Database:** MySQL with complete schema
- ✅ **API Integration:** Frontend connected to backend

## 📦 What's Been Created

### Frontend Files (in `/workspace/`)
```
index.html      - Main application UI
styles.css      - Complete responsive styling (15KB)
app.js          - Application logic with API integration
```

### Backend Files (in `/workspace/inputs/taskmanager/`)

**Controllers** (REST API endpoints):
- `AuthController.java` - Login/Signup endpoints
- `ProjectController.java` - Project CRUD operations
- `TaskController.java` - Task CRUD operations
- `UserController.java` - User lookup endpoints

**Services** (Business Logic):
- `AuthService.java` - Authentication logic
- `ProjectService.java` - Project operations
- `TaskService.java` - Task operations

**Models** (Database Entities):
- `User.java` - User entity with JPA annotations
- `Project.java` - Project entity with many-to-many relationships
- `Task.java` - Task entity with foreign keys

**Repositories** (Data Access):
- `UserRepository.java` - User CRUD interface
- `ProjectRepository.java` - Project CRUD interface
- `TaskRepository.java` - Task CRUD interface

**Configuration**:
- `CorsConfig.java` - CORS configuration for frontend access
- `application.properties` - Database and server settings
- `schema.sql` - Complete database schema with sample data

## 🚀 How to Run Everything

### Step 1: Setup Database

**Option A: Using MySQL directly**
```bash
# Connect to MySQL
mysql -u root -p

# Create database and load schema
SOURCE /workspace/inputs/taskmanager/src/main/resources/schema.sql;

# Verify
USE taskmanager_db;
SHOW TABLES;
```

**Option B: Let Hibernate create tables automatically**
- Just start the backend - it will auto-create tables (ddl-auto=update)

### Step 2: Run Backend (Spring Boot)

```bash
# Navigate to project
cd /workspace/inputs/taskmanager

# Build with Maven
mvn clean install

# Run the application
mvn spring-boot:run

# Expected output:
# Tomcat started on port(s): 8080
# Application 'taskmanager' is running!
```

**Backend URL:** `http://localhost:8080`

### Step 3: Run Frontend

```bash
# Navigate to workspace
cd /workspace

# Start web server
python3 -m http.server 8000

# Expected output:
# Serving HTTP on 0.0.0.0 port 8000
```

**Frontend URL:** `http://localhost:8000`

### Step 4: Access Application

Open browser to: **http://localhost:8000**

Login with:
- Email: `john@example.com`
- Password: `password123`

## 🔌 API Architecture

### Frontend → Backend Communication

```
Frontend (JavaScript)
    ↓ (Fetch API)
API_BASE_URL = http://localhost:8080/api
    ↓
Spring Boot REST Controllers
    ↓
Services (Business Logic)
    ↓
Repositories (JPA)
    ↓
MySQL Database
```

### Frontend ApiHelper Class

The frontend has a built-in `ApiHelper` class that handles:
- Request/Response formatting
- Authentication token management
- Error handling
- CORS requests

```javascript
// Usage in frontend:
const response = await ApiHelper.post('/auth/login', { email, password });
const tasks = await ApiHelper.get('/tasks/project/1');
```

## 📋 Database Schema

### Relationships

```
Users (1) ─── (Many) Projects
           └─ Owner relationship

Users (Many) ─── (Many) Projects
           └─ Member relationship (project_members table)

Users (1) ─── (Many) Tasks
           └─ Assigned to relationship

Projects (1) ─── (Many) Tasks
           └─ Contain tasks
```

### Tables Created

1. **users** - User accounts
2. **projects** - Projects owned by users
3. **project_members** - Many-to-many relationship
4. **tasks** - Tasks within projects

### Sample Data Inserted

```
3 Users:
- john@example.com (Admin)
- jane@example.com (Member)
- mike@example.com (Member)

2 Projects:
- Website Redesign (owner: John)
- Mobile App Development (owner: Jane)

5 Tasks:
- Assigned to different team members
- Various status and priorities
```

## 🔐 Authentication Flow

### Login Process

```javascript
1. User enters email & password
2. Frontend sends: POST /api/auth/login
3. Backend validates credentials
4. Backend returns: { token, user }
5. Frontend stores token in sessionStorage
6. Frontend uses token for all subsequent requests
```

### Token Management

```javascript
// Stored in sessionStorage
sessionStorage.setItem('authToken', 'token_...');
sessionStorage.setItem('currentUserId', '1');

// Automatically added to headers
headers['Authorization'] = `Bearer ${token}`;
```

## 🧪 Testing the API

### Using curl

**Test Login:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"john@example.com","password":"password123"}'
```

**Get All Projects:**
```bash
curl http://localhost:8080/api/projects
```

**Get Project Tasks:**
```bash
curl http://localhost:8080/api/tasks/project/1
```

### Using Postman

1. Import API endpoints
2. Set base URL: `http://localhost:8080/api`
3. Test each endpoint

### Using Browser DevTools

1. Open DevTools (F12)
2. Go to Network tab
3. Perform actions in app
4. View API calls and responses

## ⚙️ Configuration Details

### Frontend Configuration (`app.js`)

```javascript
// Change this to connect to different backend
const API_BASE_URL = 'http://localhost:8080/api';

// Demo credentials
// john@example.com / password123
// jane@example.com / password123
// mike@example.com / password123
```

### Backend Configuration (`application.properties`)

```properties
# Database Connection
spring.datasource.url=jdbc:mysql://localhost:3306/taskmanager_db
spring.datasource.username=root
spring.datasource.password=

# Server Port
server.port=8080

# Hibernate (Auto-create tables)
spring.jpa.hibernate.ddl-auto=update

# CORS Allowed Origins
# Configured in CorsConfig.java - allows localhost:8000
```

### CORS Configuration (`CorsConfig.java`)

```java
registry.addMapping("/api/**")
    .allowedOrigins("http://localhost:8000", "http://localhost:3000", "*")
    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
    .allowedHeaders("*")
    .allowCredentials(true);
```

## 📊 API Endpoints Reference

### Authentication
```
POST   /api/auth/login       - Login user
POST   /api/auth/signup      - Create new account
```

### Projects
```
GET    /api/projects          - List all projects
GET    /api/projects/{id}     - Get project details
POST   /api/projects          - Create project
POST   /api/projects/{id}/members/{userId} - Add member
DELETE /api/projects/{id}/members/{userId} - Remove member
```

### Tasks
```
GET    /api/tasks/project/{projectId}              - All tasks
GET    /api/tasks/project/{projectId}/status/{s}   - Tasks by status
GET    /api/tasks/{id}                              - Task details
POST   /api/tasks                                   - Create task
PUT    /api/tasks/{id}                              - Update task
DELETE /api/tasks/{id}                              - Delete task
```

### Users
```
GET    /api/users/{id}            - Get user by ID
GET    /api/users/email/{email}   - Get user by email
```

## 🎨 Frontend Features

### Pages
1. **Login Page** - Authentication
2. **Signup Page** - User registration
3. **Dashboard** - Project list with progress
4. **Project Page** - Tasks and team management
5. **Modals** - Create project/task/add member

### Features
- ✅ Real-time task filtering
- ✅ Team member management
- ✅ Task status tracking
- ✅ Priority levels (low/medium/high)
- ✅ Due date management
- ✅ Toast notifications
- ✅ Responsive design
- ✅ Modern UI with animations

## 🐛 Common Issues & Solutions

### Issue 1: Connection Refused (Backend)
**Problem:** `ERR_CONNECTION_REFUSED` when frontend tries to connect

**Solutions:**
1. Verify backend is running: `http://localhost:8080` in browser
2. Check port 8080 is available: `lsof -i :8080`
3. Rebuild and restart: `mvn clean install && mvn spring-boot:run`

### Issue 2: CORS Error
**Problem:** `CORS policy: No 'Access-Control-Allow-Origin'`

**Solution:**
- Backend CORS is configured for port 8000
- If frontend on different port, update `CorsConfig.java`:
  ```java
  .allowedOrigins("http://localhost:YOUR_PORT")
  ```

### Issue 3: Database Connection Failed
**Problem:** `Communication link failure`

**Solutions:**
1. Verify MySQL is running
2. Check connection string in `application.properties`
3. Create database: `CREATE DATABASE taskmanager_db;`
4. Run schema.sql to create tables

### Issue 4: Login Fails
**Problem:** "Invalid email or password"

**Solutions:**
1. Use correct demo credentials
2. Check if sample data was loaded
3. Verify database has users table with data:
   ```sql
   USE taskmanager_db;
   SELECT * FROM users;
   ```

## 📈 Development Workflow

### Making Changes

**Backend:**
1. Edit Java files
2. Rebuild: `mvn clean install`
3. Restart: `mvn spring-boot:run`
4. Test via Postman or browser DevTools

**Frontend:**
1. Edit HTML/CSS/JS files
2. Refresh browser (Ctrl+R)
3. Check console for errors (F12)
4. Test API calls in Network tab

### Debugging

**Backend:**
- Check console output from `mvn spring-boot:run`
- Enable debug logging in `application.properties`
- Add breakpoints in IDE (IntelliJ IDEA, VS Code)

**Frontend:**
- Open DevTools (F12)
- Check Console tab for errors
- Check Network tab for API calls
- Use `console.log()` for debugging

## 🚢 Production Deployment

### Backend (Spring Boot JAR)

```bash
# Build standalone JAR
mvn clean package

# Run JAR with environment variables
java -Dspring.datasource.url=jdbc:mysql://db-host:3306/taskmanager_db \
     -Dspring.datasource.username=prod_user \
     -Dspring.datasource.password=prod_password \
     -jar target/taskmanager-0.0.1-SNAPSHOT.jar
```

### Frontend (Static Files)

```bash
# Deploy to any static hosting
# Examples:
# - GitHub Pages
# - Netlify
# - Vercel
# - AWS S3
# - Azure Static Web Apps

# Don't forget to update API_BASE_URL in app.js
const API_BASE_URL = 'https://api.production.com/api';
```

## 📝 Next Steps

### Immediate
1. ✅ Run backend: `mvn spring-boot:run`
2. ✅ Run frontend: `python3 -m http.server 8000`
3. ✅ Open browser: `http://localhost:8000`
4. ✅ Login and test features

### Enhancement Ideas
- Add JWT token authentication
- Implement password hashing (bcrypt)
- Add user role permissions
- Email notifications
- Task comments/attachments
- Activity logs
- Advanced filtering/search
- Dashboard analytics
- Real-time updates (WebSocket)

### Security Improvements
- Use JWT instead of simple tokens
- Hash passwords with bcrypt
- Add HTTPS/TLS
- Input validation & sanitization
- Rate limiting
- OWASP compliance
- Security headers (CSP, HSTS, etc.)

## 📞 Quick Reference

| Command | Purpose | Port |
|---------|---------|------|
| `mvn spring-boot:run` | Run backend | 8080 |
| `python3 -m http.server 8000` | Run frontend | 8000 |
| `mysql -u root -p` | Connect to MySQL | 3306 |
| `mvn clean install` | Build backend | - |

## 🎓 Learning Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [REST API Best Practices](https://restfulapi.net/)
- [MySQL Documentation](https://dev.mysql.com/doc/)
- [JavaScript Fetch API](https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API)
- [CORS Explained](https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS)

---

**Happy Coding! 🚀**

For detailed API documentation, check the README.md file.
