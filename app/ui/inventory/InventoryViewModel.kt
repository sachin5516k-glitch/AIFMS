package com.ai.franchise.ui.inventory

import com.ai.franchise.ui.base.BaseViewModel
import com.ai.franchise.ui.base.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class InventoryViewModel : BaseViewModel<List<InventoryItem>>() {

    private val _items = MutableStateFlow<List<InventoryItem>>(emptyList())
    
    private val _isFormValid = MutableStateFlow(false)
    val isFormValid = _isFormValid.asStateFlow()

    fun loadInventory() {
        launchDataLoad {
            // Mock API Fetch
            kotlinx.coroutines.delay(500)
            val list = listOf(
                InventoryItem("1", "Coffee Beans (kg)", 50.0),
                InventoryItem("2", "Milk (L)", 20.0),
                InventoryItem("3", "Sugar (kg)", 10.0),
                InventoryItem("4", "Cups (pcs)", 500.0)
            )
            _items.value = list
            list
        }
    }

    fun validateForm(items: List<InventoryItem>) {
        // Valid if all items have a closing stock entered
        val valid = items.all { it.closingStock != null }
        _isFormValid.value = valid
    }

    fun submitInventory() {
        val itemsToSubmit = _items.value
        launchDataLoad {
            kotlinx.coroutines.delay(1000)
            // Submit API logic here
            // repository.submitInventory(itemsToSubmit)
            itemsToSubmit
        }
    }
}
