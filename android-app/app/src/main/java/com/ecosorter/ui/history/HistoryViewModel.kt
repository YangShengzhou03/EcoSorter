package com.ecosorter.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecosorter.data.model.GarbageItem
import com.ecosorter.data.local.dao.GarbageItemDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val garbageItemDao: GarbageItemDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        loadHistory()
    }

    private fun loadHistory() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            garbageItemDao.getAll()
                .collect { items ->
                    _uiState.update {
                        it.copy(
                            items = items,
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun deleteItem(item: GarbageItem) {
        viewModelScope.launch {
            garbageItemDao.delete(item)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            garbageItemDao.deleteAll()
        }
    }

    fun refresh() {
        loadHistory()
    }
}

data class HistoryUiState(
    val items: List<GarbageItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)