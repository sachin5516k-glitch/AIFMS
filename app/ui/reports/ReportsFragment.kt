package com.ai.franchise.ui.reports

import android.widget.Toast
import com.ai.franchise.databinding.FragmentReportsBinding
import com.ai.franchise.ui.base.BaseFragment
import com.ai.franchise.ui.base.UiState

class ReportsFragment : BaseFragment<FragmentReportsBinding, ReportsViewModel>(FragmentReportsBinding::inflate) {

    override val viewModel: ReportsViewModel by lazy { ReportsViewModel() }

    override fun setupUI() {
        binding.btnDownload.setOnClickListener {
            viewModel.downloadReport()
        }
    }

    override fun handleSuccess(state: UiState.Success<*>) {
        val fileName = state.data as String
        Toast.makeText(context, "Downloaded: $fileName", Toast.LENGTH_LONG).show()
        // Intent to open file would go here
    }
    
    override fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
        binding.btnDownload.isEnabled = !isLoading
    }
}
