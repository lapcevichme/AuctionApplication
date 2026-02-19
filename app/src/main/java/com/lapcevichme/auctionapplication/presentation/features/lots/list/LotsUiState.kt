package com.lapcevichme.auctionapplication.presentation.features.lots.list

import com.lapcevichme.auctionapplication.domain.model.LotSummary

sealed interface LotsUiState {
    data object Loading : LotsUiState

    data class Success(
        val lots: List<LotSummary> = emptyList(),
        val searchQuery: String = "",
        val isSearchActive: Boolean = false
    ) : LotsUiState {
        val isEmpty: Boolean get() = lots.isEmpty()
    }

    data class Error(val message: String) : LotsUiState
}
