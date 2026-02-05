package com.ai.franchise.ui.ai

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ai.franchise.data.api.AIScore
import com.ai.franchise.data.repository.AIRepository
import kotlinx.coroutines.launch

class AIViewModel(private val repository: AIRepository) : ViewModel() {

    private val _aiScore = MutableLiveData<Result<AIScore>>()
    val aiScore: LiveData<Result<AIScore>> = _aiScore

    fun runAnalysis() {
        viewModelScope.launch {
            _aiScore.value = repository.runAnalysis()
        }
    }
}
