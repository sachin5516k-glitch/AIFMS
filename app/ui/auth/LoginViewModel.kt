package com.ai.franchise.ui.auth

import androidx.lifecycle.viewModelScope
import com.ai.franchise.data.repository.AuthRepository
import com.ai.franchise.ui.base.BaseViewModel
import com.ai.franchise.ui.base.Event
import com.ai.franchise.ui.base.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Simple data class for Navigation Result
sealed class NavigationTarget {
    object OwnerDashboard : NavigationTarget()
    object ManagerDashboard : NavigationTarget()
    object OutletDashboard : NavigationTarget()
}

class LoginViewModel(
    private val authRepository: AuthRepository
) : BaseViewModel<com.ai.franchise.data.model.User>() {

    private val _email = MutableStateFlow("")
    private val _password = MutableStateFlow("")
    
    // Derived state for button enablement
    val isLoginEnabled: kotlinx.coroutines.flow.Flow<Boolean> = kotlinx.coroutines.flow.combine(_email, _password) { e, p ->
        android.util.Patterns.EMAIL_ADDRESS.matcher(e).matches() && p.isNotBlank()
    }

    private val _navigationEvent = MutableStateFlow<Event<NavigationTarget>?>(null)
    val navigationEvent = _navigationEvent.asStateFlow()

    fun onEmailChanged(text: String) {
        _email.value = text.trim()
    }

    fun onPasswordChanged(text: String) {
        _password.value = text
    }

    fun login() {
        val email = _email.value
        val password = _password.value

        launchDataLoad {
            // Note: AuthRepository needs to be updated to be suspend-friendly if not already
            // Assuming AuthRepository.login(email, password) exists and returns User or throws Error
            val user = authRepository.login(email, password)
            
            // Determine Navigation
            when (user.role) {
                "OWNER" -> _navigationEvent.value = Event(NavigationTarget.OwnerDashboard)
                "REGIONAL_MANAGER", "MANAGER" -> _navigationEvent.value = Event(NavigationTarget.ManagerDashboard)
                else -> _navigationEvent.value = Event(NavigationTarget.OutletDashboard)
            }
            
            user // returned as Success data
        }
    }
}
