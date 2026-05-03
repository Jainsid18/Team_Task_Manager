// ============================================
// Team Task Manager - Vanilla JS (FIXED)
// ============================================

const API_BASE_URL = 'http://localhost:8080';

// ================= API HELPER =================
class ApiHelper {
    static async request(endpoint, options = {}) {
        const url = `${API_BASE_URL}${endpoint}`;

        const headers = {
            'Content-Type': 'application/json',
            ...options.headers
        };

        const token = sessionStorage.getItem('authToken');
        if (token) {
            headers['Authorization'] = `Bearer ${token}`;
        }

        const response = await fetch(url, {
            ...options,
            headers,
            credentials: 'include'
        });

        if (!response.ok) {
            if (response.status === 401) {
                sessionStorage.clear();
            }
            throw new Error(`API Error: ${response.status}`);
        }

        return await response.json();
    }

    static get(endpoint) {
        return this.request(endpoint, { method: 'GET' });
    }

    static post(endpoint, data) {
        return this.request(endpoint, {
            method: 'POST',
            body: JSON.stringify(data)
        });
    }

    static put(endpoint, data) {
        return this.request(endpoint, {
            method: 'PUT',
            body: JSON.stringify(data)
        });
    }

    static delete(endpoint) {
        return this.request(endpoint, { method: 'DELETE' });
    }
}

// ================= DATA MANAGER =================
class AppDataManager {

    // AUTH
    async login(email, password) {
        const res = await ApiHelper.post('/auth/login', { email, password });

        if (res.token) {
            sessionStorage.setItem('authToken', res.token);
            sessionStorage.setItem('currentUserId', res.user.id);
        }

        return res;
    }

    async signup(name, email, password, role) {
        const res = await ApiHelper.post('/auth/signup', {
            name, email, password, role
        });

        if (res.token) {
            sessionStorage.setItem('authToken', res.token);
            sessionStorage.setItem('currentUserId', res.user.id);
        }

        return res;
    }

    // USER
    async getUser(id) {
        return await ApiHelper.get(`/users/${id}`);
    }

    async getUserByEmail(email) {
        return await ApiHelper.get(`/users/email/${email}`);
    }

    // PROJECTS
    async getProjects() {
        return await ApiHelper.get('/projects');
    }

    async createProject(name, description, ownerId) {
        return await ApiHelper.post('/projects', {
            name, description, ownerId
        });
    }

    async getProject(id) {
        return await ApiHelper.get(`/projects/${id}`);
    }

    async addTeamMember(projectId, memberId) {
        return await ApiHelper.post(`/projects/${projectId}/members`, {
            memberId
        });
    }

    async removeTeamMember(projectId, memberId) {
        return await ApiHelper.delete(`/projects/${projectId}/members/${memberId}`);
    }

    async getProjectMembers(projectId) {
        return await ApiHelper.get(`/projects/${projectId}/members`);
    }

    // TASKS
    async getTasks(projectId, filter = 'all') {
        const endpoint =
            filter === 'all'
                ? `/tasks/project/${projectId}`
                : `/tasks/project/${projectId}/status/${filter}`;

        return await ApiHelper.get(endpoint);
    }

    async createTask(data) {
        return await ApiHelper.post('/tasks', data);
    }

    async updateTask(id, data) {
        return await ApiHelper.put(`/tasks/${id}`, data);
    }

    async deleteTask(id) {
        return await ApiHelper.delete(`/tasks/${id}`);
    }
}

// ================= MAIN APP =================
class TaskManagerApp {
    constructor() {
        this.dataManager = new AppDataManager();
        this.currentUser = null;
        this.currentProject = null;
        this.currentFilter = 'all';
        this.init();
    }

    init() {
        this.setupEventListeners();
        this.checkAuthState();
    }

    setupEventListeners() {

        document.getElementById('login-form')
            .addEventListener('submit', (e) => this.handleLogin(e));

        document.getElementById('signup-form')
            .addEventListener('submit', (e) => this.handleSignup(e));
    }

    // ================= AUTH =================
    async checkAuthState() {
        const userId = sessionStorage.getItem('currentUserId');

        if (userId) {
            this.currentUser = await this.dataManager.getUser(userId);
            this.showApp();
        } else {
            this.showAuth();
        }
    }

    async handleLogin(e) {
        e.preventDefault();

        const email = document.getElementById('login-email').value;
        const password = document.getElementById('login-password').value;

        try {
            const res = await this.dataManager.login(email, password);
            this.currentUser = res.user;

            this.showApp();
        } catch (err) {
            alert("Login failed");
        }
    }

    async handleSignup(e) {
        e.preventDefault();

        const name = document.getElementById('signup-name').value;
        const email = document.getElementById('signup-email').value;
        const password = document.getElementById('signup-password').value;
        const role = document.getElementById('signup-role').value;

        const res = await this.dataManager.signup(name, email, password, role);
        this.currentUser = res.user;

        this.showApp();
    }

    logout() {
        sessionStorage.clear();
        this.currentUser = null;
        this.showAuth();
    }

    // ================= UI =================
    showAuth() {
        document.getElementById('auth-container').style.display = 'flex';
        document.getElementById('app-container').style.display = 'none';
    }

    showApp() {
        document.getElementById('auth-container').style.display = 'none';
        document.getElementById('app-container').style.display = 'block';

        document.getElementById('user-name').textContent =
            this.currentUser.name;

        this.loadDashboard();
    }

    // ================= DASHBOARD =================
    async loadDashboard() {
        const projects = await this.dataManager.getProjects();

        const grid = document.getElementById('projects-grid');
        grid.innerHTML = '';

        projects.forEach(p => {
            const div = document.createElement('div');
            div.className = 'project-card';

            div.innerHTML = `
                <h3>${p.name}</h3>
                <p>${p.description}</p>
            `;

            div.onclick = () => this.openProject(p.id);

            grid.appendChild(div);
        });
    }

    // ================= PROJECT =================
    async openProject(id) {
        this.currentProject = await this.dataManager.getProject(id);

        document.getElementById('project-title').innerText =
            this.currentProject.name;

        await this.loadTasks();
    }

    async loadTasks() {
        const tasks = await this.dataManager.getTasks(
            this.currentProject.id,
            this.currentFilter
        );

        const container = document.getElementById('tasks-list');
        container.innerHTML = '';

        tasks.forEach(t => {
            const div = document.createElement('div');
            div.className = 'task-card';

            div.innerHTML = `
                <h4>${t.title}</h4>
                <p>${t.description}</p>
            `;

            container.appendChild(div);
        });
    }

    // ================= HELPERS =================
    escapeHtml(text) {
        return text?.replace(/[&<>"']/g, m => ({
            '&': '&amp;',
            '<': '&lt;',
            '>': '&gt;',
            '"': '&quot;',
            "'": '&#039;'
        }[m]));
    }
}

// ================= INIT =================
document.addEventListener('DOMContentLoaded', () => {
    new TaskManagerApp();
});