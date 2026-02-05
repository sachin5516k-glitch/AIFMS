package com.ai.franchise.ui.auth

import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.ai.franchise.R
import com.ai.franchise.data.repository.AuthRepository
import com.ai.franchise.databinding.FragmentLoginBinding
import com.ai.franchise.ui.base.BaseFragment
import com.ai.franchise.ui.base.BaseViewModel
import com.ai.franchise.ui.base.UiState
import kotlinx.coroutines.launch

class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>(FragmentLoginBinding::inflate) {

    // Ideally use Hilt/Koin for injection. For now, manual injection.
    override val viewModel: LoginViewModel by lazy {
        LoginViewModel(AuthRepository(requireContext()))
    }

    override fun setupUI() {
        // Input Listeners
        binding.etEmail.doAfterTextChanged { 
            binding.tilEmail.error = null // Clear error on edit
            viewModel.onEmailChanged(it.toString()) 
        }
        
        binding.etPassword.doAfterTextChanged { 
            binding.tilPassword.error = null
            viewModel.onPasswordChanged(it.toString()) 
        }

        // Login Button
        binding.btnLogin.setOnClickListener {
            // Unfocus inputs to hide keyboard logic could go here
            viewModel.login()
        }
        
        // Observe Button Enable State
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoginEnabled.collect { isEnabled ->
                    binding.btnLogin.isEnabled = isEnabled
                }
            }
        }
        
        // Observe Navigation
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigationEvent.collect { event ->
                    event?.getContentIfNotHandled()?.let { target ->
                        navigateToDashboard(target)
                    }
                }
            }
        }
    }
    
    private fun navigateToDashboard(target: NavigationTarget) {
        val actionId = when(target) {
            is NavigationTarget.OwnerDashboard -> R.id.action_login_to_ownerDashboard
            is NavigationTarget.ManagerDashboard -> R.id.action_login_to_managerDashboard
            is NavigationTarget.OutletDashboard -> R.id.action_login_to_outletDashboard
        }
        findNavController().navigate(actionId)
    }

    override fun handleSuccess(state: UiState.Success<*>) {
        // Success handled via Navigation Event, but we could show a toast here
        // or ensure loading is dismissed (handled by BaseFragment)
    }

    override fun showLoading(isLoading: Boolean) {
        binding.loadingOverlay.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
        binding.btnLogin.isEnabled = !isLoading // Disable button while loading
    }
    
    override fun showError(message: String) {
        super.showError(message)
        // Specific error handling for fields could go here if the error message implied it
        binding.tilPassword.error = "Check credentials"
        binding.tilEmail.error = " " // generic indicator
    }
}
