package com.lapcevichme.auctionapplication.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PageResponseDto<T> (
    val content: List<T>,
    val last : Boolean
)
