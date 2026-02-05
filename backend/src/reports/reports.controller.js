const Sales = require('../sales/Sales');
const Franchise = require('../franchise/Franchise');
const { generateAuditReport } = require('./audit.service');

// @desc    Get Royalty Report
// @route   GET /api/reports/royalty
// @access  Private (Owner only)
exports.getRoyaltyReport = async (req, res) => {
    try {
        const { month, year } = req.query;

        // Default to current month if not specified
        const date = new Date();
        const queryMonth = month ? parseInt(month) - 1 : date.getMonth();
        const queryYear = year ? parseInt(year) : date.getFullYear();

        const startDate = new Date(queryYear, queryMonth, 1);
        const endDate = new Date(queryYear, queryMonth + 1, 0);

        // 1. Get all stats grouped by Franchise
        const royaltyStats = await Sales.aggregate([
            {
                $match: {
                    createdAt: { $gte: startDate, $lte: endDate }
                }
            },
            {
                $group: {
                    _id: "$franchiseId",
                    totalSales: { $sum: "$amount" },
                    transactionCount: { $sum: 1 }
                }
            },
            {
                $lookup: {
                    from: "franchises",
                    localField: "_id",
                    foreignField: "_id",
                    as: "franchise"
                }
            },
            {
                $unwind: "$franchise"
            },
            {
                $project: {
                    franchiseName: "$franchise.name",
                    totalSales: 1,
                    transactionCount: 1,
                    royaltyPercentage: "$franchise.royaltyPercentage",
                    calculatedRoyalty: {
                        $multiply: ["$totalSales", { $divide: ["$franchise.royaltyPercentage", 100] }]
                    }
                }
            }
        ]);

        res.json({
            period: `${queryMonth + 1}-${queryYear}`,
            data: royaltyStats
        });

    } catch (error) {
        res.status(500).json({ message: error.message });
    }
};

// @desc    Download Audit PDF
// @route   GET /api/reports/audit/:franchiseId/download
exports.downloadAuditReport = async (req, res) => {
    try {
        const { franchiseId } = req.params;
        await generateAuditReport(franchiseId, res);
    } catch (error) {
        console.error(error);
        res.status(500).send('Error generating report');
    }
};
