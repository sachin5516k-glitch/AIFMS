const Attendance = require('./Attendance');
const Franchise = require('../franchise/Franchise');

// Calculate distance helper (Same as Sales)
function getDistanceFromLatLonInM(lat1, lon1, lat2, lon2) {
    const R = 6371;
    const dLat = deg2rad(lat2 - lat1);
    const dLon = deg2rad(lon2 - lon1);
    const a =
        Math.sin(dLat / 2) * Math.sin(dLat / 2) +
        Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
        Math.sin(dLon / 2) * Math.sin(dLon / 2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    const d = R * c;
    return d * 1000;
}

function deg2rad(deg) {
    return deg * (Math.PI / 180);
}

// @desc    Check In
// @route   POST /api/attendance/checkin
// @access  Private (Staff/Manager)
exports.checkIn = async (req, res) => {
    try {
        const { latitude, longitude } = req.body;
        const franchiseId = req.user.franchiseId;

        if (!req.file) {
            return res.status(400).json({ message: 'Selfie proof is required' });
        }

        const franchise = await Franchise.findById(franchiseId);
        if (!franchise) return res.status(404).json({ message: 'Franchise not found' });

        // Geofence Check
        const dist = getDistanceFromLatLonInM(latitude, longitude, franchise.coordinates.latitude, franchise.coordinates.longitude);
        if (dist > franchise.coordinates.radius) {
            return res.status(403).json({ message: `You are too far from the outlet to check in. Distance: ${Math.round(dist)}m` });
        }

        // Check if already checked in today
        const startOfDay = new Date();
        startOfDay.setHours(0, 0, 0, 0);
        const existing = await Attendance.findOne({
            userId: req.user._id,
            date: { $gte: startOfDay }
        });

        if (existing) {
            return res.status(400).json({ message: 'Already checked in for today' });
        }

        const attendance = await Attendance.create({
            userId: req.user._id,
            franchiseId,
            checkInTime: new Date(),
            checkInLocation: { latitude, longitude },
            checkInProofId: req.file.id,
            date: new Date() // Store date specifically for querying
        });

        res.status(201).json(attendance);
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
};

// @desc    Check Out
// @route   POST /api/attendance/checkout
// @access  Private
exports.checkOut = async (req, res) => {
    try {
        const { latitude, longitude } = req.body;
        const franchiseId = req.user.franchiseId;

        if (!req.file) {
            return res.status(400).json({ message: 'Selfie proof is required' });
        }

        const startOfDay = new Date();
        startOfDay.setHours(0, 0, 0, 0);

        // Find today's check-in
        const attendance = await Attendance.findOne({
            userId: req.user._id,
            date: { $gte: startOfDay },
            checkOutTime: null // Not yet checked out
        });

        if (!attendance) {
            return res.status(400).json({ message: 'No active check-in found for today' });
        }

        // Geofence Check (Optional: Allow checkout from anywhere? No, stricter is better)
        const franchise = await Franchise.findById(franchiseId);
        const dist = getDistanceFromLatLonInM(latitude, longitude, franchise.coordinates.latitude, franchise.coordinates.longitude);
        // We might allow slightly larger radius for checkout or same. sticking to same.
        if (dist > franchise.coordinates.radius) {
            return res.status(403).json({ message: `You are too far from the outlet to check out. Distance: ${Math.round(dist)}m` });
        }

        const now = new Date();
        const duration = (now - attendance.checkInTime) / 1000 / 60; // minutes

        attendance.checkOutTime = now;
        attendance.checkOutLocation = { latitude, longitude };
        attendance.checkOutProofId = req.file.id;
        attendance.durationMinutes = Math.round(duration);

        await attendance.save();

        res.json(attendance);

    } catch (error) {
        res.status(500).json({ message: error.message });
    }
};
