package com.lapcevichme.auctionapplication.presentation.features.lots.details

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LotDetailsViewModel() : ViewModel() {
    private val _uiState = MutableStateFlow<LotDetailsUiState>(LotDetailsUiState.Success())
    val uiState: StateFlow<LotDetailsUiState> = _uiState.asStateFlow()
}