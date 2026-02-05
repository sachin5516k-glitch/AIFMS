const Sales = require('../sales/Sales');
const InventoryLog = require('../inventory/InventoryLog');
const Attendance = require('../users/Attendance');
const AIScore = require('./AIScore');

class AIService {

    // Helper: Calculate Mean
    calculateMean(data) {
        if (!data || data.length === 0) return 0;
        return data.reduce((a, b) => a + b, 0) / data.length;
    }

    // Helper: Calculate Standard Deviation
    calculateStdDev(data, mean) {
        if (!data || data.length === 0) return 0;
        return Math.sqrt(data.map(x => Math.pow(x - mean, 2)).reduce((a, b) => a + b, 0) / data.length);
    }

    // Helper: Calculate Linear Regression Slope
    calculateSlope(yValues) {
        if (!yValues || yValues.length < 2) return 0;
        const n = yValues.length;
        const xValues = Array.from({ length: n }, (_, i) => i); // 0, 1, 2...

        const sumX = this.calculateMean(xValues) * n;
        const sumY = this.calculateMean(yValues) * n;
        const sumXY = xValues.reduce((sum, x, i) => sum + x * yValues[i], 0);
        const sumXX = xValues.reduce((sum, x) => sum + x * x, 0);

        return (n * sumXY - sumX * sumY) / (n * sumXX - sumX * sumX);
    }

    async analyzeFranchise(franchiseId) {
        const today = new Date();
        const thirtyDaysAgo = new Date();
        thirtyDaysAgo.setDate(today.getDate() - 30);

        // 1. Fetch Data (Sales, Inventory, Attendance)
        const salesData = await Sales.find({
            franchiseId,
            createdAt: { $gte: thirtyDaysAgo }
        }).sort({ createdAt: 1 });

        const inventoryData = await InventoryLog.find({
            franchiseId,
            date: { $gte: thirtyDaysAgo }
        });

        // 2. Fraud Detection (Z-Score on Sales)
        // Group sales by day first
        const dailySalesMap = {};
        salesData.forEach(s => {
            const d = s.createdAt.toISOString().split('T')[0];
            dailySalesMap[d] = (dailySalesMap[d] || 0) + s.amount;
        });
        const dailySales = Object.values(dailySalesMap);

        const meanSales = this.calculateMean(dailySales);
        const stdDevSales = this.calculateStdDev(dailySales, meanSales);

        let fraudScore = 0;
        const anomalies = [];

        // Check if today's sales (or last entry) is anomalous
        if (dailySales.length > 0) {
            const lastSales = dailySales[dailySales.length - 1];
            const zScore = stdDevSales === 0 ? 0 : (lastSales - meanSales) / stdDevSales;

            if (Math.abs(zScore) > 2) { // 2 Sigma rule
                fraudScore += 50;
                anomalies.push(zScore > 0 ? "Abnormal Sales Spike" : "Abnormal Sales Drop");
            }
        }

        // 3. Health Score Calculation
        // Metric A: Sales Consistency (StdDev inversed) -> Lower deviation is better
        // Metric B: Inventory Variance -> Lower is better
        // Metric C: Active Days -> More is better

        let healthScore = 100;

        // Penalty for high sales volatility
        if (stdDevSales > meanSales * 0.5) healthScore -= 20;

        // Penalty for Inventory Variance
        // (Simplified: check last log for variance)
        if (inventoryData.length > 0) {
            const lastLog = inventoryData[inventoryData.length - 1];
            const totalVariance = lastLog.items.reduce((sum, item) => sum + Math.abs(item.variance || 0), 0);
            if (totalVariance > 10) {
                healthScore -= 30;
                anomalies.push("High Inventory Variance");
                fraudScore += 20;
            }
        }

        // 4. Failure Prediction (Slope)
        const slope = this.calculateSlope(dailySales);
        let failureRisk = 0;
        if (slope < -10) { // arbitrary threshold, e.g., dropping 10 units per day on avg
            failureRisk = 60;
            anomalies.push("Declining Sales Trend");
        }
        if (healthScore < 50) failureRisk += 30;

        // 5. Recommendations
        const recommendations = [];
        if (fraudScore > 40) recommendations.push("Conduct Surprise Audit");
        if (anomalies.includes("High Inventory Variance")) recommendations.push("Check Staff Theft / Wastage");
        if (slope < 0) recommendations.push("Review Marketing Strategy");
        if (recommendations.length === 0) recommendations.push("Operations are healthy");

        // Save Score
        const score = await AIScore.create({
            franchiseId,
            fraudScore: Math.min(100, fraudScore),
            healthScore: Math.max(0, healthScore),
            failureRisk: Math.min(100, failureRisk),
            anomalies,
            recommendations
        });

        return score;
    }
}

module.exports = new AIService();
