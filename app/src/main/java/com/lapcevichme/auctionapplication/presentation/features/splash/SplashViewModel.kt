package com.lapcevichme.auctionapplication.presentation.features.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lapcevichme.auctionapplication.domain.usecase.auth.LoginCheckUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

sealed interface SplashEvent {
    data object ToMain : SplashEvent
    data object ToLogin : SplashEvent
}

class SplashViewModel(
    private val loginCheckUseCase: LoginCheckUseCase
) : ViewModel() {

    private val _events = Channel<SplashEvent>()
    val events = _events.receiveAsFlow()

    init {
        checkAuth()
    }

    private fun checkAuth() {
        viewModelScope.launch {
            val minDelay = launch { delay(500) }

            val hasCredentials = loginCheckUseCase()

            minDelay.join()

            if (hasCredentials) {
                _events.send(SplashEvent.ToMain)
            } else {
                _events.send(SplashEvent.ToLogin)
            }
        }
    }
}