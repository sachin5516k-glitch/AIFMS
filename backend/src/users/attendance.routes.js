const express = require('express');
const router = express.Router();
const { checkIn, checkOut } = require('./attendance.controller');
const { protect } = require('../middleware/auth.middleware');
const upload = require('../middleware/upload.middleware');

router.post('/checkin', protect, upload.single('selfie'), checkIn);
router.post('/checkout', protect, upload.single('selfie'), checkOut);

module.exports = router;
