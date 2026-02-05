const express = require('express');
const router = express.Router();
const { submitSales, getSales } = require('./sales.controller');
const { protect } = require('../middleware/auth.middleware');
const upload = require('../middleware/upload.middleware');

router.post('/', protect, upload.single('proof'), submitSales);
router.get('/', protect, getSales);

module.exports = router;
