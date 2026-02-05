const express = require('express');
const cors = require('cors');
const dotenv = require('dotenv');
const mongoose = require('mongoose');

// Route Imports
const authRoutes = require('./auth/auth.routes');
const salesRoutes = require('./sales/sales.routes');
const inventoryRoutes = require('./inventory/inventory.routes');
const governanceRoutes = require('./governance/governance.routes');
const attendanceRoutes = require('./users/attendance.routes');
const reportsRoutes = require('./reports/reports.routes');
const dashboardRoutes = require('./dashboard/dashboard.routes');
const franchiseRoutes = require('./franchise/franchise.routes');

dotenv.config();

const app = express();

// Middleware
app.use(cors());
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// Database Connection
mongoose.connect(process.env.MONGO_URI, {
    useNewUrlParser: true,
    useUnifiedTopology: true
})
    .then(() => console.log('✅ MongoDB Connected'))
    .catch(err => {
        console.error('❌ MongoDB Connection Error:', err);
        // In production, we might want to exit or retry
        // process.exit(1); 
    });

// API Routes
app.use('/api/auth', authRoutes);
app.use('/api/sales', salesRoutes);
app.use('/api/inventory', inventoryRoutes);
app.use('/api/governance', governanceRoutes); // includes dispute, ai insights
app.use('/api/attendance', attendanceRoutes);
app.use('/api/reports', reportsRoutes);
app.use('/api/dashboard', dashboardRoutes);
app.use('/api/franchise', franchiseRoutes);

// Health Check
app.get('/health', (req, res) => {
    const dbStatus = mongoose.connection.readyState === 1 ? 'CONNECTED' : 'DISCONNECTED';
    res.status(200).json({
        status: 'UP',
        timestamp: new Date(),
        database: dbStatus,
        uptime: process.uptime()
    });
});

app.get('/', (req, res) => res.send('AI Franchise Backend Running (Production Mode)'));

const PORT = process.env.PORT || 5000;
app.listen(PORT, () => console.log(`Server running on port ${PORT}`));
