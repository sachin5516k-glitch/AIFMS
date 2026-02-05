const mongoose = require('mongoose');

const inventoryItemSchema = new mongoose.Schema({
    franchiseId: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Franchise',
        required: true
    },
    name: {
        type: String,
        required: true
    },
    unit: {
        type: String,
        required: true // e.g., 'kg', 'ltr', 'pcs'
    },
    minStockThreshold: {
        type: Number,
        default: 10
    },
    currentStock: {
        type: Number,
        default: 0
    },
    pricePerUnit: {
        type: Number,
        default: 0
    }
});

module.exports = mongoose.model('InventoryItem', inventoryItemSchema);
