package com.ai.franchise.ui.dashboard

import com.ai.franchise.databinding.FragmentOwnerDashboardBinding
import com.ai.franchise.ui.base.BaseFragment
import com.ai.franchise.ui.base.UiState

class OwnerDashboardFragment : BaseFragment<FragmentOwnerDashboardBinding, OwnerViewModel>(FragmentOwnerDashboardBinding::inflate) {

    // Manual Injection
    override val viewModel: OwnerViewModel by lazy { OwnerViewModel() }

    override fun setupUI() {
        viewModel.loadData()
    }

    override fun handleSuccess(state: UiState.Success<*>) {
        val data = state.data as OwnerStats
        
        binding.tvRevenue.text = "$${data.totalRevenue}"
        binding.tvOutlets.text = data.activeOutlets.toString()
        binding.tvRisk.text = data.highRiskCount.toString()

        binding.chartRevenue.setData(data.revenueTrend)
        binding.chartRisk.setData(data.riskDistribution)
    }

    override fun showLoading(isLoading: Boolean) {
        binding.loader.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
    }
}
