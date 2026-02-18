package com.lapcevichme.auctionapplication.data.remote.dto.lot

import java.time.OffsetDateTime

data class LotCreateRequest(
    val title: String,
    val description: String?,
    val pictureUrl: String?,
    val originalPrice: Long,
    val expirationDate: OffsetDateTime,
)