package com.lapcevichme.auctionapplication.presentation.features.lots.details

import com.lapcevichme.auctionapplication.domain.model.lot.Lot
import com.lapcevichme.auctionapplication.domain.model.bid.Bid

sealed interface LotDetailsUiState {
    data object Loading : LotDetailsUiState

    data class Success(
        val lot: Lot? = null,
        val bids: List<Bid> = emptyList()
    ) : LotDetailsUiState

    data class Error(val message: String) : LotDetailsUiState
}