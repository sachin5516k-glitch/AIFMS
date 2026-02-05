const mongoose = require('mongoose');

const complianceSchema = new mongoose.Schema({
    franchiseId: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Franchise',
        required: true
    },
    date: {
        type: Date,
        required: true // Store as pure date (YYYY-MM-DD) or start of day
    },
    salesSubmitted: {
        type: Boolean,
        default: false
    },
    inventorySubmitted: {
        type: Boolean,
        default: false
    },
    staffAttendanceCount: {
        type: Number,
        default: 0
    },
    violations: [{
        rule: String, // e.g., "No Sales Entry", "Low Staff"
        severity: String // "WARNING", "CRITICAL"
    }]
});

module.exports = mongoose.model('Compliance', complianceSchema);
