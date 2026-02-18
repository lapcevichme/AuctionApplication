package com.lapcevichme.auctionapplication.domain.model

import java.time.OffsetDateTime

data class Bid(
    val id: String,
    val bidder: User,
    val amount: Long,
    val time: OffsetDateTime
)