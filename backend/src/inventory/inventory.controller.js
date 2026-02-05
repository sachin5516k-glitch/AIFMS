const InventoryItem = require('./InventoryItem');
const InventoryLog = require('./InventoryLog');

// @desc    Add new inventory item
// @route   POST /api/inventory/items
// @access  Private (Owner/Manager)
exports.addItem = async (req, res) => {
    try {
        const { name, unit, minStockThreshold, pricePerUnit } = req.body;
        const franchiseId = req.user.franchiseId;

        const item = await InventoryItem.create({
            franchiseId,
            name,
            unit,
            minStockThreshold,
            pricePerUnit
        });
        res.status(201).json(item);
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
};

// @desc    Get all inventory items
// @route   GET /api/inventory/items
// @access  Private
exports.getItems = async (req, res) => {
    try {
        const items = await InventoryItem.find({ franchiseId: req.user.franchiseId });
        res.json(items);
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
};

// @desc    Submit Daily Stock (Opening/Closing)
// @route   POST /api/inventory/log
// @access  Private (Manager)
exports.submitDailyLog = async (req, res) => {
    try {
        const { items } = req.body; // Array of { itemId, openingStock, closingStock }
        const franchiseId = req.user.franchiseId;

        const logItems = items.map(item => ({
            itemId: item.itemId,
            openingStock: item.openingStock,
            closingStock: item.closingStock,
            consumption: item.openingStock - item.closingStock
        }));

        const log = await InventoryLog.create({
            franchiseId,
            items: logItems,
            submittedBy: req.user._id
        });

        // Update current stock in Item definition
        for (const item of items) {
            await InventoryItem.findByIdAndUpdate(item.itemId, {
                currentStock: item.closingStock
            });
        }

        res.status(201).json(log);
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
};
