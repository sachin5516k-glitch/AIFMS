package com.ai.franchise.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ai.franchise.data.repository.AuthRepository

class ViewModelFactory(private val repository: AuthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(com.ai.franchise.ui.auth.LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return com.ai.franchise.ui.auth.LoginViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
