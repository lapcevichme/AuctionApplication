package com.lapcevichme.auctionapplication.domain.model

import java.time.OffsetDateTime

data class CreateLotParam(
    val title: String,
    val description: String?,
    val pictureUrl: String?,
    val originalPrice: Long,
    val expirationDate: OffsetDateTime
)