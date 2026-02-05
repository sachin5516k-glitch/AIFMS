package com.ai.franchise.ui.attendance

import android.graphics.Bitmap
import com.ai.franchise.ui.base.BaseViewModel
import com.ai.franchise.ui.base.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AttendanceViewModel : BaseViewModel<String>() {

    private val _isCheckedIn = MutableStateFlow(false)
    val isCheckedIn = _isCheckedIn.asStateFlow()
    
    private val _selfieBitmap = MutableStateFlow<Bitmap?>(null)
    
    private val _isActionEnabled = MutableStateFlow(false)
    val isActionEnabled = _isActionEnabled.asStateFlow()

    fun onSelfieCaptured(bitmap: Bitmap) {
        _selfieBitmap.value = bitmap
        _isActionEnabled.value = true
    }

    fun toggleAttendance() {
        val currentStatus = _isCheckedIn.value
        launchDataLoad {
            kotlinx.coroutines.delay(1000)
            if (currentStatus) {
                // Check Out
                _isCheckedIn.value = false
                _selfieBitmap.value = null // Reset for next time
                _isActionEnabled.value = false
                "Checked Out Successfully"
            } else {
                // Check In
                _isCheckedIn.value = true
                _selfieBitmap.value = null
                _isActionEnabled.value = false
                "Checked In Successfully"
            }
        }
    }
}
