package com.lapcevichme.auctionapplication.data.remote.dto.auth

import com.lapcevichme.auctionapplication.domain.model.UserBalance
import com.lapcevichme.auctionapplication.domain.model.UserRole

data class UserPatch(
    val name: String?,
    val role: UserRole?,
    val balance: UserBalance?
)