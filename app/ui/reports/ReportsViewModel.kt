package com.ai.franchise.ui.reports

import com.ai.franchise.ui.base.BaseViewModel

class ReportsViewModel : BaseViewModel<String>() {

    fun downloadReport() {
        launchDataLoad {
            kotlinx.coroutines.delay(2000)
            "Report_Oct_2023.pdf" // Return file path/name
        }
    }
}
