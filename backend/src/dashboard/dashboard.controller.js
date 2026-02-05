const Sales = require('../sales/Sales');
const AIScore = require('../ai/AIScore');
const Compliance = require('../governance/Compliance');
const Franchise = require('../franchise/Franchise');

// @desc    Get Dashboard Data (Owner)
// @route   GET /api/dashboard/owner
exports.getOwnerDashboard = async (req, res) => {
    try {
        // 1. Total Sales (All Time or Current Month)
        const totalSales = await Sales.aggregate([
            { $group: { _id: null, total: { $sum: "$amount" } } }
        ]);

        // 2. High Risk Franchises
        const riskyFranchises = await AIScore.find({ fraudScore: { $gt: 50 } })
            .populate('franchiseId', 'name location')
            .sort({ date: -1 })
            .limit(5);

        // 3. Franchise List with Health
        const franchises = await Franchise.find().select('name location');

        res.json({
            totalSales: totalSales[0]?.total || 0,
            riskyFranchises,
            franchiseCount: franchises.length
        });
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
};

// @desc    Get Dashboard Data (Regional Manager)
// @route   GET /api/dashboard/manager/:franchiseId
exports.getManagerDashboard = async (req, res) => {
    try {
        const { franchiseId } = req.params;

        // 1. Sales Trend (Last 7 Days)
        const sevenDaysAgo = new Date();
        sevenDaysAgo.setDate(sevenDaysAgo.getDate() - 7);

        const salesTrend = await Sales.aggregate([
            { $match: { franchiseId: new mongoose.Types.ObjectId(franchiseId), createdAt: { $gte: sevenDaysAgo } } },
            { $group: { _id: { $dateToString: { format: "%Y-%m-%d", date: "$createdAt" } }, total: { $sum: "$amount" } } },
            { $sort: { _id: 1 } }
        ]);

        // 2. Compliance Alerts
        const compliance = await Compliance.findOne({ franchiseId }).sort({ date: -1 });

        // 3. Latest AI Score
        const aiScore = await AIScore.findOne({ franchiseId }).sort({ date: -1 });

        res.json({
            salesTrend,
            complianceStatus: compliance ? compliance.violations : [],
            healthScore: aiScore ? aiScore.healthScore : 100,
            fraudRisk: aiScore ? aiScore.fraudScore : 0
        });

    } catch (error) {
        res.status(500).json({ message: error.message });
    }
};
