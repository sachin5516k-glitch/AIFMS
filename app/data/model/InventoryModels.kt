package com.ai.franchise.data.model

data class InventoryItem(
    val _id: String,
    val name: String,
    val unit: String,
    val currentStock: Double,
    val minStockThreshold: Double
)

data class InventoryLogRequest(
    val items: List<LogItem>
)

data class LogItem(
    val itemId: String,
    val openingStock: Double,
    val closingStock: Double
)
