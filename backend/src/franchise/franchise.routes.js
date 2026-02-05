const express = require('express');
const router = express.Router();
const { createFranchise, getFranchises, getFranchiseById } = require('./franchise.controller');
const { protect, authorize } = require('../middleware/auth.middleware');

router.route('/')
    .post(protect, authorize('OWNER'), createFranchise)
    .get(protect, authorize('OWNER', 'REGIONAL_MANAGER'), getFranchises);

router.route('/:id')
    .get(protect, getFranchiseById);

module.exports = router;
