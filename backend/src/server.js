const express = require('express');
const cors = require('cors');
const dotenv = require('dotenv');

dotenv.config();

const app = express();
app.use(cors());
app.use(express.json());

// Routes
app.get('/', (req, res) => res.send('AI Franchise Backend Running'));

// Health Check for Uptime Monitors (Render/UptimeRobot)
app.get('/health', (req, res) => {
    // In real scenario, also check DB connection state here
    res.status(200).json({
        status: 'UP',
        timestamp: new Date(),
        uptime: process.uptime()
    });
});

// Mock Logic for Demo (Since we aren't connecting to real DB in this snippet)
app.post('/api/auth/login', (req, res) => {
    const { email } = req.body;
    let role = "STAFF";
    let name = "Staff Member";

    if (email.includes("owner")) {
        role = "OWNER";
        name = "Franchise Owner";
    } else if (email.includes("manager")) {
        role = "MANAGER";
        name = "Outlet Manager";
    }

    res.json({
        id: "user_" + Date.now(),
        name: name,
        email: email,
        role: role,
        token: "mock_jwt_token_" + Date.now()
    });
});

app.get('/api/dashboard/owner', (req, res) => {
    res.json({
        totalRevenue: "125000",
        activeFranchises: "3",
        avgHealthScore: "88"
    });
});

const PORT = process.env.PORT || 5000;
app.listen(PORT, () => console.log(`Server running on port ${PORT}`));
