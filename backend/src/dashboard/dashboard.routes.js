const express = require('express');
const router = express.Router();
const { getOwnerDashboard, getManagerDashboard } = require('./dashboard.controller');
const { protect, authorize } = require('../middleware/auth.middleware');
const mongoose = require('mongoose'); // Needed for ObjectId casting if not handled in controller

router.get('/owner', protect, authorize('OWNER'), getOwnerDashboard);
router.get('/manager/:franchiseId', protect, getManagerDashboard);

module.exports = router;
