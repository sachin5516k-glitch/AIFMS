package com.ai.franchise.ui.ai

import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.ai.franchise.databinding.FragmentAiInsightsBinding
import com.ai.franchise.ui.base.BaseFragment
import com.ai.franchise.ui.base.UiState

class AIInsightsFragment : BaseFragment<FragmentAiInsightsBinding, AIInsightsViewModel>(FragmentAiInsightsBinding::inflate) {

    override val viewModel: AIInsightsViewModel by lazy { AIInsightsViewModel() }
    private lateinit var adapter: RiskAdapter

    override fun setupUI() {
        adapter = RiskAdapter { risk ->
            // Navigate to Dispute (To be implemented)
            Toast.makeText(context, "Navigating to dispute: ${risk.title}", Toast.LENGTH_SHORT).show()
        }
        
        binding.rvRisks.layoutManager = LinearLayoutManager(context)
        binding.rvRisks.adapter = adapter
        
        viewModel.loadInsights()
    }

    override fun handleSuccess(state: UiState.Success<*>) {
        val data = state.data as AIHealthData
        binding.tvScore.text = "${data.score}/100"
        binding.tvStatus.text = data.status
        adapter.submitList(data.risks)
    }

    override fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
    }
}
