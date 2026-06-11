package com.scanwise.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.scanwise.app.data.repository.ScanRepository
import com.scanwise.app.data.repository.ScanStats
import com.scanwise.app.domain.model.AnalysisResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppViewModel(private val repository: ScanRepository) : ViewModel() {

    private val _lastResult = MutableStateFlow<AnalysisResult?>(null)
    val lastResult: StateFlow<AnalysisResult?> = _lastResult.asStateFlow()

    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing: StateFlow<Boolean> = _isAnalyzing.asStateFlow()

    private val _stats = MutableStateFlow(ScanStats(0, 0, 0, 0))
    val stats: StateFlow<ScanStats> = _stats.asStateFlow()

    val historyFlow = repository.historyFlow
    val blacklistFlow = repository.blacklistFlow

    fun scanUrl(url: String, onDone: (AnalysisResult) -> Unit) {
        if (_isAnalyzing.value) return
        viewModelScope.launch {
            _isAnalyzing.value = true
            val result = repository.analyze(url)
            _lastResult.value = result
            _isAnalyzing.value = false
            refreshStats()
            onDone(result)
        }
    }

    fun blockDomain(domain: String, url: String) {
        viewModelScope.launch { repository.blockDomain(domain, url) }
    }

    fun deleteScans(ids: List<Long>) {
        viewModelScope.launch { repository.deleteScans(ids) }
    }

    fun refreshStats() {
        viewModelScope.launch { _stats.value = repository.stats() }
    }

    init {
        refreshStats()
    }

    companion object {
        fun factory(repository: ScanRepository) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                AppViewModel(repository) as T
        }
    }
}
