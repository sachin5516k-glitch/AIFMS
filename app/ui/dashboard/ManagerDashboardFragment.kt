package com.ai.franchise.ui.dashboard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.ai.franchise.R
import com.ai.franchise.ui.widgets.SimpleBarChart

class ManagerDashboardFragment : Fragment(R.layout.fragment_manager_dashboard) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val chart = view.findViewById<SimpleBarChart>(R.id.chartComparison)
        
        // Mock Data directly for phase completeness
        chart.setData(mapOf(
            "Outlet A" to 5000f,
            "Outlet B" to 7500f,
            "Outlet C" to 3000f
        ))
    }
}
