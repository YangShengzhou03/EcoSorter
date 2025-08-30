package com.ecosorter.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecosorter.data.model.GarbageItem
import com.ecosorter.data.local.dao.GarbageItemDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val garbageItemDao: GarbageItemDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadRecentItems()
    }

    private fun loadRecentItems() {
        viewModelScope.launch {
            garbageItemDao.getRecentItems()
                .collect { items ->
                    _uiState.update { it.copy(recentItems = items) }
                }
        }
    }

    fun refreshData() {
        loadRecentItems()
    }

    fun deleteItem(item: GarbageItem) {
        viewModelScope.launch {
            garbageItemDao.delete(item)
        }
    }
}

data class HomeUiState(
    val recentItems: List<GarbageItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)