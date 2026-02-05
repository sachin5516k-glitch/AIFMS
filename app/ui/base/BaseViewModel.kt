package com.ai.franchise.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<T> : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<T>>(UiState.Idle)
    val uiState: StateFlow<UiState<T>> = _uiState.asStateFlow()

    protected fun setState(state: UiState<T>) {
        _uiState.value = state
    }

    protected fun launchDataLoad(block: suspend () -> T) {
        viewModelScope.launch {
            setState(UiState.Loading)
            try {
                val result = block()
                setState(UiState.Success(result))
            } catch (e: Exception) {
                e.printStackTrace()
                setState(UiState.Error(e.message ?: "Unknown Error"))
            }
        }
    }
}
