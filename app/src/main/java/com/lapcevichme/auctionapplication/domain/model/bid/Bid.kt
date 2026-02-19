package com.lapcevichme.auctionapplication.domain.model.bid

import com.lapcevichme.auctionapplication.domain.model.user.User
import java.time.OffsetDateTime

data class Bid(
    val id: String,
    val bidder: User,
    val amount: Long,
    val time: OffsetDateTime
)