const mongoose = require('mongoose');

const aiScoreSchema = new mongoose.Schema({
    franchiseId: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Franchise',
        required: true
    },
    date: {
        type: Date,
        default: Date.now
    },
    fraudScore: {
        type: Number,
        default: 0 // 0-100, Higher is bad
    },
    healthScore: {
        type: Number,
        default: 100 // 0-100, Higher is good
    },
    failureRisk: {
        type: Number,
        default: 0 // 0-100% Probability
    },
    anomalies: [{
        type: String // e.g., "High Sales Variance", "Inventory Mismatch"
    }],
    recommendations: [{
        type: String // e.g., "Audit required", "Check staff roster"
    }]
});

module.exports = mongoose.model('AIScore', aiScoreSchema);
