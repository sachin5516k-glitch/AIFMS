package com.ai.franchise.ui.sales

import android.graphics.Bitmap
import com.ai.franchise.ui.base.BaseViewModel
import com.ai.franchise.ui.base.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SalesViewModel : BaseViewModel<Boolean>() {

    private val _amount = MutableStateFlow("")
    private val _proofImage = MutableStateFlow<Bitmap?>(null)
    private val _isLocationVerified = MutableStateFlow(false) // Mocked for now

    // Derived State: Valid if amount > 0 and image exists
    val isFormValid = combine(_amount, _proofImage, _isLocationVerified) { amount, image, loc ->
        amount.toDoubleOrNull() ?: 0.0 > 0 && image != null
    }

    fun onAmountChanged(text: String) {
        _amount.value = text
    }

    fun onProofCaptured(bitmap: Bitmap) {
        _proofImage.value = bitmap
    }
    
    fun onLocationVerified() {
        _isLocationVerified.value = true
    }

    fun getTodayDate(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    fun submitSales() {
        val amount = _amount.value.toDoubleOrNull() ?: return
        val image = _proofImage.value ?: return

        launchDataLoad {
            // Simulate API Call
            kotlinx.coroutines.delay(2000)
            
            // Logic to upload image and submit data would go here
            // repository.submitSales(amount, image, location)
            
            true // Success
        }
    }
}
