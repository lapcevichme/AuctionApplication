package com.lapcevichme.auctionapplication.presentation.features.lots.list

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LotsViewModel() : ViewModel() {
    private val _uiState = MutableStateFlow<LotsUiState>(LotsUiState.Success())
    val uiState: StateFlow<LotsUiState> = _uiState.asStateFlow()
}