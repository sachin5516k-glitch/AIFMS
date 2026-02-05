const express = require('express');
const router = express.Router();
const { getRoyaltyReport, downloadAuditReport } = require('./reports.controller');
const { protect, authorize } = require('../middleware/auth.middleware');

router.get('/royalty', protect, authorize('OWNER', 'REGIONAL_MANAGER'), getRoyaltyReport);
router.get('/audit/:franchiseId/download', protect, downloadAuditReport);


module.exports = router;
