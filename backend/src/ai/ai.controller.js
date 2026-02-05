const aiService = require('./ai.service');
const AIScore = require('./AIScore');

// @desc    Run AI Analysis manually (or scheduled)
// @route   POST /api/ai/analyze
// @access  Private (Owner/Regional)
exports.runAnalysis = async (req, res) => {
    try {
        const franchiseId = req.user.franchiseId || req.body.franchiseId;
        if (!franchiseId && req.user.role === 'OWNER') {
            // For Owner, maybe analyze a specific passed ID or all?
            // Simplification: Require ID in body if Owner
            return res.status(400).json({ message: 'Franchise ID required' });
        }

        const score = await aiService.analyzeFranchise(franchiseId);
        res.json(score);
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
};

// @desc    Get Latest Insights
// @route   GET /api/ai/insights/:franchiseId
// @access  Private
exports.getInsights = async (req, res) => {
    try {
        // Find latest score
        const score = await AIScore.findOne({ franchiseId: req.params.franchiseId })
            .sort({ date: -1 });

        if (!score) {
            return res.status(404).json({ message: 'No analysis found. Run analysis first.' });
        }
        res.json(score);
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
};
