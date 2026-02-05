const mongoose = require('mongoose');

const attendanceSchema = new mongoose.Schema({
    userId: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User',
        required: true
    },
    franchiseId: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Franchise',
        required: true
    },
    checkInTime: {
        type: Date
    },
    checkOutTime: {
        type: Date
    },
    checkInLocation: {
        latitude: Number,
        longitude: Number
    },
    checkInProofId: {
        type: mongoose.Schema.Types.ObjectId // GridFS ID for selfie
    },
    checkOutLocation: {
        latitude: Number,
        longitude: Number
    },
    checkOutProofId: {
        type: mongoose.Schema.Types.ObjectId
    },
    durationMinutes: {
        type: Number,
        default: 0
    },
    status: {
        type: String,
        enum: ['PRESENT', 'ABSENT', 'HALF_DAY'],
        default: 'PRESENT'
    },
    date: {
        type: Date,
        default: Date.now
    }
});

module.exports = mongoose.model('Attendance', attendanceSchema);
