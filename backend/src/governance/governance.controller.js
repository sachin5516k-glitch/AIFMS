const Dispute = require('./Dispute');
const Compliance = require('./Compliance');
const Sales = require('../sales/Sales');
const { runDailyAnalysis } = require('../ai/ai.service'); // We might trigger AI too

// @desc    Raise a Dispute
// @route   POST /api/governance/dispute
exports.raiseDispute = async (req, res) => {
    try {
        const { type, referenceId, reason } = req.body;
        const dispute = await Dispute.create({
            franchiseId: req.user.franchiseId,
            userId: req.user._id,
            type,
            referenceId,
            reason
        });
        res.status(201).json(dispute);
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
};

// @desc    Get Compliance Status
// @route   GET /api/governance/compliance
exports.getCompliance = async (req, res) => {
    try {
        const franchiseId = req.user.franchiseId;
        // Just get last 7 days
        const sevenDaysAgo = new Date();
        sevenDaysAgo.setDate(sevenDaysAgo.getDate() - 7);

        const logs = await Compliance.find({
            franchiseId,
            date: { $gte: sevenDaysAgo }
        }).sort({ date: -1 });

        res.json(logs);
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
};
