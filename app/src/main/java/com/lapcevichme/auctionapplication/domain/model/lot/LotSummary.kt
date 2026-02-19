package com.lapcevichme.auctionapplication.domain.model.lot

import com.lapcevichme.auctionapplication.domain.model.user.User
import com.lapcevichme.auctionapplication.domain.model.bid.Bid
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