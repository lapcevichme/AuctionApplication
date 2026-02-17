package com.lapcevichme.auctionapplication.presentation.utlis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun ViewModel.launchWithLoading(
    loadingState: (Boolean) -> Unit,
    block: suspend () -> Unit
): Job {
    return viewModelScope.launch {
        try {
            loadingState(true)
            block()
        } finally {
            loadingState(false)
        }
    }
}