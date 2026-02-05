const Sales = require('./Sales');
const Franchise = require('../franchise/Franchise');

// Calculate distance between two coordinates in meters (Haversine formula)
function getDistanceFromLatLonInM(lat1, lon1, lat2, lon2) {
    const R = 6371; // Radius of the earth in km
    const dLat = deg2rad(lat2 - lat1);
    const dLon = deg2rad(lon2 - lon1);
    const a =
        Math.sin(dLat / 2) * Math.sin(dLat / 2) +
        Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
        Math.sin(dLon / 2) * Math.sin(dLon / 2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    const d = R * c; // Distance in km
    return d * 1000; // Distance in meters
}

function deg2rad(deg) {
    return deg * (Math.PI / 180);
}

// @desc    Submit daily sales with proof
// @route   POST /api/sales
// @access  Private (Franchise Manager/Owner)
exports.submitSales = async (req, res) => {
    try {
        const { amount, latitude, longitude } = req.body;
        const franchiseId = req.user.franchiseId;

        if (!req.file) {
            return res.status(400).json({ message: 'Proof image is required' });
        }

        if (!franchiseId) {
            return res.status(400).json({ message: 'User is not assigned to a franchise' });
        }

        // 1. Get Franchise Location
        const franchise = await Franchise.findById(franchiseId);
        if (!franchise) {
            return res.status(404).json({ message: 'Franchise not found' });
        }

        // 2. Validate Geofence
        const distance = getDistanceFromLatLonInM(
            latitude,
            longitude,
            franchise.coordinates.latitude,
            franchise.coordinates.longitude
        );

        if (distance > franchise.coordinates.radius) {
            return res.status(403).json({
                message: `Location mismatch! You are ${Math.round(distance)}m away from the outlet. range: ${franchise.coordinates.radius}m`
            });
        }

        // 3. Create Sales Record
        const sales = await Sales.create({
            franchiseId,
            userId: req.user._id,
            amount,
            proofImageId: req.file.id, // GridFS file ID
            location: {
                latitude,
                longitude
            },
            isVerified: true // Auto-verified by geofence for now (or set to false for manual review)
        });

        res.status(201).json(sales);
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
};

// @desc    Get sales history
// @route   GET /api/sales
// @access  Private
exports.getSales = async (req, res) => {
    try {
        let query = {};

        // If regional manager or owner, they might want to filter by franchiseId
        if (req.user.role === 'FRANCHISE_MANAGER' || req.user.role === 'STAFF') {
            query = { franchiseId: req.user.franchiseId };
        } else if (req.query.franchiseId) {
            query = { franchiseId: req.query.franchiseId };
        }

        const sales = await Sales.find(query).sort({ createdAt: -1 });
        res.json(sales);
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
};
