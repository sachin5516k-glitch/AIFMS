const Sales = require('../sales/Sales');
const AIScore = require('../ai/AIScore');
const Franchise = require('../franchise/Franchise');

exports.generateAuditReport = async (franchiseId, res) => {
    // Generate CSV instead of PDF to avoid 'pdfkit' dependency
    res.setHeader('Content-Type', 'text/csv');
    res.setHeader('Content-Disposition', `attachment; filename=audit_report_${franchiseId}.csv`);

    const franchise = await Franchise.findById(franchiseId);
    const scores = await AIScore.find({ franchiseId }).sort({ date: -1 }).limit(1);
    const latestScore = scores[0] || {};

    // CSV Header
    let csv = 'Report Type,Franchise Name,Date,Health Score,Fraud Probability,Failure Risk\n';
    csv += `Audit Report,${franchise ? franchise.name : 'Unknown'},${new Date().toLocaleDateString()},${latestScore.healthScore || 'N/A'},${latestScore.fraudScore || 'N/A'}%,${latestScore.failureRisk || 'N/A'}%\n`;

    csv += '\n--- Recommendations ---\n';
    if (latestScore.recommendations) {
        latestScore.recommendations.forEach(r => csv += `Recommendation,${r}\n`);
    }

    csv += '\n--- Detected Anomalies ---\n';
    if (latestScore.anomalies) {
        latestScore.anomalies.forEach(a => csv += `Anomaly,${a}\n`);
    }

    res.send(csv);
};
