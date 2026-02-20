package com.lapcevichme.auctionapplication.presentation.features.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.lapcevichme.auctionapplication.domain.model.lot.CreateLotParam
import com.lapcevichme.auctionapplication.domain.model.user.UserBalance
import com.lapcevichme.auctionapplication.domain.model.user.UserMe
import com.lapcevichme.auctionapplication.domain.model.user.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.OffsetDateTime

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        _uiState.value = ProfileUiState.Success(
            user = UserMe(
                email = "oatis@example.com",
                name = "Oat",
                role = UserRole.SELLER,
                avatarUrl = null,
                balance = UserBalance(
                    available = 50000,
                    frozen = 1200,
                    scale = 2
                )
            )
        )
    }

    fun toggleCreateDialog(show: Boolean) {
        _uiState.update { state ->
            if (state is ProfileUiState.Success) state.copy(showCreateDialog = show) else state
        }
    }

    fun updateLotTitle(title: String) {
        _uiState.update { state ->
            if (state is ProfileUiState.Success) state.copy(lotTitle = title) else state
        }
    }

    fun updateLotDescription(description: String) {
        _uiState.update { state ->
            if (state is ProfileUiState.Success) state.copy(lotDescription = description) else state
        }
    }

    fun updateLotPrice(price: String) {
        if (price.all { it.isDigit() }) {
            _uiState.update { state ->
                if (state is ProfileUiState.Success) state.copy(lotPrice = price) else state
            }
        }
    }

    fun updateLotImage(uri: Uri?) {
        _uiState.update { state ->
            if (state is ProfileUiState.Success) state.copy(lotImageUri = uri) else state
        }
    }

    fun createLot() {
        val state = _uiState.value as? ProfileUiState.Success ?: return

        val param = CreateLotParam(
            title = state.lotTitle,
            description = state.lotDescription.ifBlank { null },
            pictureUrl = state.lotImageUri?.toString(),
            originalPrice = state.lotPrice.toLongOrNull() ?: 0L,
            expirationDate = OffsetDateTime.now().plusDays(7)
        )

        executeCreateLot(param)
    }

    private fun executeCreateLot(param: CreateLotParam) {
        toggleCreateDialog(false)
        clearLotFields()
    }

    private fun clearLotFields() {
        _uiState.update { state ->
            if (state is ProfileUiState.Success) {
                state.copy(
                    lotTitle = "",
                    lotDescription = "",
                    lotPrice = "",
                    lotImageUri = null
                )
            } else state
        }
    }

    fun logout() {
    }
}