const mongoose = require('mongoose');

const inventoryLogSchema = new mongoose.Schema({
    franchiseId: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Franchise',
        required: true
    },
    date: {
        type: Date,
        default: Date.now
    },
    items: [{
        itemId: {
            type: mongoose.Schema.Types.ObjectId,
            ref: 'InventoryItem',
            required: true
        },
        openingStock: {
            type: Number,
            required: true
        },
        closingStock: {
            type: Number,
            required: true
        },
        consumption: {
            type: Number, // opening - closing
            required: true
        },
        variance: {
            type: Number, // calculated based on sales vs consumption (AI Phase will use this)
            default: 0
        }
    }],
    submittedBy: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User'
    }
});

module.exports = mongoose.model('InventoryLog', inventoryLogSchema);
