package com.ai.franchise.ui.sales

import android.Manifest
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.ai.franchise.databinding.FragmentSalesSubmissionBinding
import com.ai.franchise.ui.base.BaseFragment
import com.ai.franchise.ui.base.UiState
import kotlinx.coroutines.launch

class SalesSubmissionFragment : BaseFragment<FragmentSalesSubmissionBinding, SalesViewModel>(FragmentSalesSubmissionBinding::inflate) {

    override val viewModel: SalesViewModel by lazy { SalesViewModel() }
    
    // Camera Result
    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
        bitmap?.let {
            viewModel.onProofCaptured(it)
            binding.ivProof.setImageBitmap(it)
            binding.ivProof.setPadding(0,0,0,0) // Remove padding to show full image
            binding.tvTapToCapture.visibility = View.GONE
        }
    }
    
    // Permission Request
    private val requestCamera = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) takePicture.launch(null) else Toast.makeText(context, "Camera permission required for proof", Toast.LENGTH_SHORT).show()
    }

    override fun setupUI() {
        binding.etDate.setText("${viewModel.getTodayDate()} (Today)")
        
        // Mock Location Verify
        viewModel.onLocationVerified()
        binding.chipLocation.text = "Location: Verified"
        
        // Listeners
        binding.etAmount.doAfterTextChanged { 
            viewModel.onAmountChanged(it.toString())
        }
        
        binding.ivProof.setOnClickListener {
            requestCamera.launch(Manifest.permission.CAMERA)
        }
        
        binding.btnSubmit.setOnClickListener {
            viewModel.submitSales()
        }
        
        // Observers
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isFormValid.collect { isValid ->
                    binding.btnSubmit.isEnabled = isValid
                }
            }
        }
    }

    override fun handleSuccess(state: UiState.Success<*>) {
        Toast.makeText(context, "Sales Submitted Successfully!", Toast.LENGTH_LONG).show()
        // Navigate back or reset form
        binding.etAmount.setText("")
        binding.ivProof.setImageResource(android.R.drawable.ic_menu_camera)
    }

    override fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.btnSubmit.isEnabled = !isLoading
        binding.etAmount.isEnabled = !isLoading
    }
}
