const Franchise = require('./Franchise');

// @desc    Create a new franchise
// @route   POST /api/franchises
// @access  Private (Owner only)
exports.createFranchise = async (req, res) => {
    try {
        const { name, location, ownerId, coordinates } = req.body;

        const franchise = await Franchise.create({
            name,
            location,
            ownerId,
            coordinates
        });

        res.status(201).json(franchise);
    } catch (error) {
        res.status(400).json({ message: error.message });
    }
};

// @desc    Get all franchises
// @route   GET /api/franchises
// @access  Private (Owner/Regional Manager)
exports.getFranchises = async (req, res) => {
    try {
        const franchises = await Franchise.find().populate('ownerId', 'name email');
        res.json(franchises);
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
};

// @desc    Get franchise by ID
// @route   GET /api/franchises/:id
// @access  Private
exports.getFranchiseById = async (req, res) => {
    try {
        const franchise = await Franchise.findById(req.params.id);
        if (!franchise) {
            return res.status(404).json({ message: 'Franchise not found' });
        }
        res.json(franchise);
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
};
