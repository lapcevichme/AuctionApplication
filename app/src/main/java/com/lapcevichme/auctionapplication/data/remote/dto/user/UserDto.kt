package com.lapcevichme.auctionapplication.data.remote.dto.user

import com.lapcevichme.auctionapplication.domain.model.user.User
import com.lapcevichme.auctionapplication.domain.model.user.UserRole
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: String,
    val name: String,
    val role: String,
    val avatarUrl: String?
)

internal fun UserDto.toDomain() : User {
    return User(
        id = this.id,
        name = this.name,
        role = UserRole.valueOf(this.role),
        avatarUrl = this.avatarUrl
    )
}