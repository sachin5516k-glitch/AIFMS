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

// Mock Data / API Routes (Placeholder for actual logic)
// In a real migration, we would port logic from server.py or proxy to it.
// For Prompt 3 compliance (deployable Node backend), we establish this entry point.

const PORT = process.env.PORT || 5000;
app.listen(PORT, () => console.log(`Server running on port ${PORT}`));
