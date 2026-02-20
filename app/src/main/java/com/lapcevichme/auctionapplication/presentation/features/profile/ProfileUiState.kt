package com.lapcevichme.auctionapplication.presentation.features.profile

import android.net.Uri
import com.lapcevichme.auctionapplication.domain.model.user.UserMe

sealed interface ProfileUiState {
    data object Loading : ProfileUiState

    data class Success(
        val user: UserMe,
        val showCreateDialog: Boolean = false,
        val lotTitle: String = "",
        val lotDescription: String = "",
        val lotPrice: String = "",
        val lotImageUri: Uri? = null
    ) : ProfileUiState

    data class Error(val message: String) : ProfileUiState
}