package com.lapcevichme.auctionapplication.domain.model

import java.time.OffsetDateTime

data class LotSummary(
    val id: String,
    val title: String,
    val description: String?,
    val pictureUrl: String?,
    val creator: User,
    val currentPrice: Long,
    val highestBid: Bid?,
    val createdAt: OffsetDateTime,
    val expirationDate: OffsetDateTime,
    val status: LotStatus
)