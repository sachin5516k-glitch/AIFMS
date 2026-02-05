package com.ai.franchise.ui.attendance

import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.ai.franchise.databinding.FragmentAttendanceBinding
import com.ai.franchise.ui.base.BaseFragment
import com.ai.franchise.ui.base.UiState
import kotlinx.coroutines.launch

class AttendanceFragment : BaseFragment<FragmentAttendanceBinding, AttendanceViewModel>(FragmentAttendanceBinding::inflate) {

    override val viewModel: AttendanceViewModel by lazy { AttendanceViewModel() }

    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
        bitmap?.let {
            viewModel.onSelfieCaptured(it)
            binding.ivSelfiePreview.setImageBitmap(it)
        }
    }

    override fun setupUI() {
        binding.fabCapture.setOnClickListener {
            takePicture.launch(null)
        }
        
        binding.btnAction.setOnClickListener {
            viewModel.toggleAttendance()
        }
        
        // Observers
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.isCheckedIn.collect { checkedIn ->
                        binding.tvStatus.text = if (checkedIn) "Status: Checked In" else "Status: Not Checked In"
                        binding.btnAction.text = if (checkedIn) "Check Out" else "Check In"
                        
                        // If checked in, button might be enabled to let them check out without new photo?
                        // Or we enforce photo for BOTH. 
                        // For this prompt, let's reset enablement based on photo.
                    }
                }
                
                launch {
                    viewModel.isActionEnabled.collect { enabled ->
                        binding.btnAction.isEnabled = enabled
                    }
                }
            }
        }
    }

    override fun handleSuccess(state: UiState.Success<*>) {
        val msg = state.data as String
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        // Clear preview
        binding.ivSelfiePreview.setImageBitmap(null)
    }
}
