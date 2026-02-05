package com.ai.franchise.ui.dashboard

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ai.franchise.R
import com.ai.franchise.data.repository.GovernanceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class DashboardFragment : Fragment() {

    private val repository = GovernanceRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        
        val btnDownload = view.findViewById<Button>(R.id.btnDownload)
        val btnDispute = view.findViewById<Button>(R.id.btnDispute)

        btnDownload.setOnClickListener {
            downloadReport()
        }

        btnDispute.setOnClickListener {
            // Simplified Dispute for Demo
            raiseDispute()
        }

        return view
    }

    private fun downloadReport() {
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val file = File(storageDir, "Audit_Report_${System.currentTimeMillis()}.pdf")
        
        Toast.makeText(context, "Downloading...", Toast.LENGTH_SHORT).show()
        
        CoroutineScope(Dispatchers.IO).launch {
            // Hardcoded franchise ID for demo
            // In real app, get from User Preferences
            val result = repository.downloadReport("YOUR_FRANCHISE_ID_HERE", file)
            withContext(Dispatchers.Main) {
                result.onSuccess {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                }.onFailure {
                    Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun raiseDispute() {
         CoroutineScope(Dispatchers.IO).launch {
             // Mock dispute
             val result = repository.raiseDispute("SALES_FLAG", "MOCK_REF_ID", "AI flagged incorrectly, sales were legit.")
             withContext(Dispatchers.Main) {
                result.onSuccess {
                    Toast.makeText(context, "Dispute Submitted!", Toast.LENGTH_SHORT).show()
                }.onFailure {
                    Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
         }
    }
}
