const express = require('express');
const router = express.Router();
const { runAnalysis, getInsights } = require('./ai.controller');
const { protect, authorize } = require('../middleware/auth.middleware');

router.post('/analyze', protect, runAnalysis);
router.get('/insights/:franchiseId', protect, getInsights);

module.exports = router;
