package com.lapcevichme.auctionapplication.domain.model.bid

import java.time.OffsetDateTime

data class BidSummary (
    val id: String,
    val amount: Long,
    val time: OffsetDateTime
)
