package com.lapcevichme.auctionapplication.presentation.features.lots.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lapcevichme.auctionapplication.domain.usecase.lots.GetLotByIdUseCase
import com.lapcevichme.auctionapplication.domain.usecase.lots.GetLotsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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

    fun loadNextPage() {
        val currentState = _uiState.value as? LotsUiState.Success ?: return
        if (currentState.isNextPageLoading || currentState.isLastPage) return

        _uiState.update { state ->
            if (state is LotsUiState.Success) state.copy(isNextPageLoading = true) else state
        }

        viewModelScope.launch {
            getLotsUseCase(page = currentPage, size = PAGE_SIZE).fold(
                onSuccess = { pagedData ->
                    _uiState.update { state ->
                        if (state is LotsUiState.Success) {
                            state.copy(
                                lots = (state.lots + pagedData.items).distinctBy { it.id }
                            )
                        } else state
                    }
                    if (!pagedData.isLastPage) currentPage++
                },
                onFailure = { error ->
                    _uiState.update { state ->
                        if (state is LotsUiState.Success) state.copy(isNextPageLoading = false) else state
                    }
                }
            )
        }
    }

    fun onSearchActiveChange(isActive: Boolean) {
        _uiState.update { state ->
            if (state is LotsUiState.Success) state.copy(isSearchActive = isActive) else state
        }
    }
}