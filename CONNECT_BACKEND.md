# Connect Frontend to Backend

## ✅ Frontend Ready
- Frontend files are in `/workspace/`
- API URL already configured to: `http://localhost:8080`

## 🚀 How to Connect

### Step 1: Start Backend (Spring Boot)
```bash
cd /workspace/inputs/taskmanager/taskmanager
mvn spring-boot:run
```

**Expected:**
- Backend runs on http://localhost:8080
- Database: MySQL on localhost:3306
- Database: taskdb
- Credentials: root / Siddhant@1811

### Step 2: Start Frontend (if not running)
```bash
cd /workspace
python3 -m http.server 8000
```

**Frontend:** http://localhost:8000

### Step 3: Open & Test
- Go to: http://localhost:8000
- Frontend will connect to backend at: http://localhost:8080
- Login with your credentials

## ✅ Connection Status

| Component | URL | Status |
|-----------|-----|--------|
| Frontend | http://localhost:8000 | ✅ Ready |
| Backend API | http://localhost:8080 | Ready to start |
| Database | localhost:3306 (taskdb) | Configured |

## 🔧 Database Info
- Host: localhost
- Port: 3306
- Database: taskdb
- Username: root
- Password: Siddhant@1811

---

**Everything is connected and ready to go!**
