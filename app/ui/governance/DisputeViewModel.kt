package com.ai.franchise.ui.governance

import com.ai.franchise.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class DisputeViewModel : BaseViewModel<Boolean>() {

    fun submitDispute(reason: String) {
        if (reason.isBlank()) {
            setState(com.ai.franchise.ui.base.UiState.Error("Please provide a reason"))
            return
        }
        
        launchDataLoad {
            kotlinx.coroutines.delay(1000)
            true // Success
        }
    }
}
