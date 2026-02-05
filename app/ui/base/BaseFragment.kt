package com.ai.franchise.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.launch

abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel<*>>(
    private val inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    protected abstract val viewModel: VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupUI()
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is UiState.Loading -> showLoading(true)
                        is UiState.Success -> {
                            showLoading(false)
                            handleSuccess(state)
                        }
                        is UiState.Error -> {
                            showLoading(false)
                            showError(state.message)
                        }
                        is UiState.Idle -> showLoading(false)
                    }
                }
            }
        }
    }

    protected open fun showLoading(isLoading: Boolean) {
        // Can be overridden by child fragments to show specific loading UI
        // Default: No-op or log
    }

    protected open fun showError(message: String, retryAction: (() -> Unit)? = null) {
        val root = view ?: return
        val snackbar = com.google.android.material.snackbar.Snackbar.make(root, message, com.google.android.material.snackbar.Snackbar.LENGTH_LONG)
        if (retryAction != null) {
            snackbar.setAction("Retry") { retryAction() }
        }
        snackbar.show()
    }

    abstract fun setupUI()
    
    // Child fragments must implement this to handle the generic success data
    abstract fun handleSuccess(state: UiState.Success<*>)

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Prevent Memory Leaks
    }
}
