const express = require('express');
const router = express.Router();
const { addItem, getItems, submitDailyLog } = require('./inventory.controller');
const { protect } = require('../middleware/auth.middleware');

router.post('/items', protect, addItem);
router.get('/items', protect, getItems);
router.post('/log', protect, submitDailyLog);

module.exports = router;
