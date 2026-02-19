package com.lapcevichme.auctionapplication.domain.model.user

data class UserBalance(
    val available: Long,
    val frozen: Long,
    val scale: Int
)