package com.lapcevichme.auctionapplication.data.remote.dto.user

import com.lapcevichme.auctionapplication.domain.model.user.UserBalance
import com.lapcevichme.auctionapplication.domain.model.user.UserRole

data class UserPatch(
    val name: String?,
    val role: UserRole?,
    val balance: UserBalance?
)