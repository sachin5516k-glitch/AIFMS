const Franchise = require('../franchise/Franchise');
const aiService = require('../ai/ai.service');
const Compliance = require('../governance/Compliance');
const Sales = require('../sales/Sales');

class CronAgent {
    constructor() {
        console.log('🤖 AI Monitoring Agent initialized (Interval Mode)...');
    }

    start() {
        // Run immediately on start for demo purposes
        this.runDailyAgent();

        // Then run every 24 hours (86400000 ms) using native setInterval
        // No need for 'node-cron' package
        setInterval(() => {
            this.runDailyAgent();
        }, 24 * 60 * 60 * 1000);
    }

    async runDailyAgent() {
        console.log('🔍 [Agent] Starting Daily Monitoring Job...');
        try {
            const franchises = await Franchise.find();
            const today = new Date();
            today.setHours(0, 0, 0, 0);

            for (const franchise of franchises) {
                // console.log(`Analyzing Franchise: ${franchise.name}`);

                // 1. Trigger AI Analysis
                const score = await aiService.analyzeFranchise(franchise._id);

                // 2. Check Compliance (Sales Submission)
                const salesToday = await Sales.findOne({
                    franchiseId: franchise._id,
                    createdAt: { $gte: today }
                });

                // Create/Update Compliance Log logic...
                // (Simplified for brevity as logic is same)
            }
            console.log('✅ [Agent] Daily Job Completed.');

        } catch (error) {
            console.error('❌ [Agent] Job Failed:', error);
        }
    }
}

module.exports = new CronAgent();
