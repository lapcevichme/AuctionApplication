package com.lapcevichme.auctionapplication.data.remote.dto.bid

import com.lapcevichme.auctionapplication.data.remote.dto.user.UserDto
import com.lapcevichme.auctionapplication.data.remote.dto.user.toDomain
import com.lapcevichme.auctionapplication.domain.model.bid.Bid
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

@Serializable
data class BidDto(
    val id: String,
    val bidder: UserDto,
    val amount: Long,
    val time: String
)

internal fun BidDto.toDomain() : Bid {
    return Bid(
        id = this.id,
        bidder = this.bidder.toDomain(),
        amount = this.amount,
        time = OffsetDateTime.parse(this.time)
    )
}