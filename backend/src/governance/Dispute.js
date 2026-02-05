const mongoose = require('mongoose');

const disputeSchema = new mongoose.Schema({
    franchiseId: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Franchise',
        required: true
    },
    userId: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User',
        required: true
    },
    type: {
        type: String,
        enum: ['SALES_FLAG', 'ATTENDANCE_FLAG', 'INVENTORY_VARIANCE', 'OTHER'],
        required: true
    },
    referenceId: {
        type: mongoose.Schema.Types.ObjectId, // ID of the Sale, Attendance, or Log being disputed
        required: true
    },
    reason: {
        type: String,
        required: true
    },
    status: {
        type: String,
        enum: ['PENDING', 'APPROVED', 'REJECTED'],
        default: 'PENDING'
    },
    managerComment: {
        type: String
    },
    createdAt: {
        type: Date,
        default: Date.now
    }
});

module.exports = mongoose.model('Dispute', disputeSchema);
