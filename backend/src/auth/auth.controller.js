const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const User = require('../users/User');

// Generate JWT
const generateToken = (id, role, franchiseId) => {
    return jwt.sign({ id, role, franchiseId }, process.env.JWT_SECRET, {
        expiresIn: '30d'
    });
};

// @desc    Register new user
// @route   POST /api/auth/register
// @access  Public (or Protected for creating managers)
exports.register = async (req, res) => {
    try {
        const { name, username, password, role, franchiseId } = req.body;

        // Check if user exists
        const userExists = await User.findOne({ username });
        if (userExists) {
            return res.status(400).json({ message: 'User already exists' });
        }

        // Hash password
        const salt = await bcrypt.genSalt(10);
        const hashedPassword = await bcrypt.hash(password, salt);

        // Create user
        const user = await User.create({
            name,
            username,
            password: hashedPassword,
            role,
            franchiseId
        });

        if (user) {
            res.status(201).json({
                _id: user._id,
                name: user.name,
                username: user.username,
                role: user.role,
                franchiseId: user.franchiseId,
                token: generateToken(user._id, user.role, user.franchiseId)
            });
        } else {
            res.status(400).json({ message: 'Invalid user data' });
        }
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
};

// @desc    Authenticate a user
// @route   POST /api/auth/login
// @access  Public
exports.login = async (req, res) => {
    try {
        const { username, password } = req.body;

        // Check for user
        const user = await User.findOne({ username });

        if (user && (await bcrypt.compare(password, user.password))) {
            res.json({
                _id: user._id,
                name: user.name,
                username: user.username,
                role: user.role,
                franchiseId: user.franchiseId,
                token: generateToken(user._id, user.role, user.franchiseId)
            });
        } else {
            res.status(401).json({ message: 'Invalid credentials' });
        }
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
};
