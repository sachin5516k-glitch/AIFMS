package com.ai.franchise.ui.inventory

import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ai.franchise.databinding.FragmentInventoryLogBinding
import com.ai.franchise.ui.base.BaseFragment
import com.ai.franchise.ui.base.UiState
import kotlinx.coroutines.launch

class InventoryLogFragment : BaseFragment<FragmentInventoryLogBinding, InventoryViewModel>(FragmentInventoryLogBinding::inflate) {

    override val viewModel: InventoryViewModel by lazy { InventoryViewModel() }
    private lateinit var adapter: InventoryAdapter

    override fun setupUI() {
        adapter = InventoryAdapter {
            // On any change, re-validate
            viewModel.validateForm(adapter.getItems())
        }
        
        binding.rvInventory.layoutManager = LinearLayoutManager(context)
        binding.rvInventory.adapter = adapter
        
        binding.btnSubmitInventory.setOnClickListener {
            viewModel.submitInventory()
        }

        viewModel.loadInventory()
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isFormValid.collect { isValid ->
                    binding.btnSubmitInventory.isEnabled = isValid
                }
            }
        }
    }

    override fun handleSuccess(state: UiState.Success<*>) {
        // Since loadInventory and submitInventory return Generic List, we check context
        val list = state.data as List<InventoryItem>
        // Check if it was a submission (all have closing stock) or load
        // Simple heuristic for demo:
        if (list.isNotEmpty() && list[0].closingStock == null) {
            // Load
            adapter.submitList(list)
        } else {
            // Submit
            Toast.makeText(context, "Inventory Logged!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
        binding.btnSubmitInventory.isEnabled = !isLoading
    }
}
