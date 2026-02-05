const express = require('express');
const router = express.Router();
const { raiseDispute, getCompliance } = require('./governance.controller');
const { protect } = require('../middleware/auth.middleware');

router.post('/dispute', protect, raiseDispute);
router.get('/compliance', protect, getCompliance);

module.exports = router;
