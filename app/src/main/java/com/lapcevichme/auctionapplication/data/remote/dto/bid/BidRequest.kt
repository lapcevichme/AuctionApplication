package com.lapcevichme.auctionapplication.data.remote.dto.bid

import kotlinx.serialization.Serializable

@Serializable
data class BidRequest(
    val amount: Long
)