package com.lapcevichme.auctionapplication.presentation.features.lots.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lapcevichme.auctionapplication.domain.usecase.lots.GetLotByIdUseCase
import com.lapcevichme.auctionapplication.domain.usecase.lots.GetLotsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LotsViewModel(
    private val getLotsUseCase: GetLotsUseCase,
    private val getLotByIdUseCase: GetLotByIdUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<LotsUiState>(LotsUiState.Loading)
    val uiState: StateFlow<LotsUiState> = _uiState.asStateFlow()

    private var currentPage = 0
    private val PAGE_SIZE = 20

    init {
        loadInitialLots()
    }

    fun loadInitialLots() {
        viewModelScope.launch {
            _uiState.value = LotsUiState.Loading
            getLotsUseCase(0, PAGE_SIZE).fold(
                onSuccess = { pagedData ->
                    currentPage = 1
                    _uiState.value = LotsUiState.Success(
                        lots = pagedData.items,
                        isLastPage = pagedData.isLastPage
                    )
                },
                onFailure = {
                    _uiState.value = LotsUiState.Error(it.message ?: "Ошибка")
                }
            )
        }
    }


}