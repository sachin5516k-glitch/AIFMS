package com.ai.franchise.ui.governance

import android.widget.Toast
import com.ai.franchise.databinding.FragmentDisputeBinding
import com.ai.franchise.ui.base.BaseFragment
import com.ai.franchise.ui.base.UiState

class DisputeFragment : BaseFragment<FragmentDisputeBinding, DisputeViewModel>(FragmentDisputeBinding::inflate) {

    override val viewModel: DisputeViewModel by lazy { DisputeViewModel() }

    override fun setupUI() {
        binding.btnSubmitDispute.setOnClickListener {
            viewModel.submitDispute(binding.etReason.text.toString())
        }
        
        binding.btnUploadEvidence.setOnClickListener {
            Toast.makeText(context, "Camera Picker (Same as Sales)", Toast.LENGTH_SHORT).show()
        }
    }

    override fun handleSuccess(state: UiState.Success<*>) {
        Toast.makeText(context, "Dispute Submitted!", Toast.LENGTH_SHORT).show()
        // popBackStack
    }
}
