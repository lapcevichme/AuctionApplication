package com.lapcevichme.auctionapplication.data.remote.dto.lot

import com.lapcevichme.auctionapplication.data.remote.dto.bid.BidDto
import com.lapcevichme.auctionapplication.data.remote.dto.bid.toDomain
import com.lapcevichme.auctionapplication.data.remote.dto.user.UserDto
import com.lapcevichme.auctionapplication.data.remote.dto.user.toDomain
import com.lapcevichme.auctionapplication.domain.model.lot.Lot
import com.lapcevichme.auctionapplication.domain.model.lot.LotStatus
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

@Serializable
data class LotDto(
    val id: String,
    val title: String,
    val description: String? = null,
    val pictureUrl: String? = null,
    val creator: UserDto,
    val currentPrice: Long,
    val originalPrice: Long,
    val minBidStep: Long,
    val highestBid: BidDto? = null,
    val createdAt: String,
    val expirationDate: String,
    val status: String
)

internal fun LotDto.toDomain(): Lot {
    return Lot(
        id = this.id,
        title = this.title,
        description = this.description,
        pictureUrl = this.pictureUrl,
        creator = this.creator.toDomain(),
        currentPrice = this.currentPrice,
        originalPrice = this.originalPrice,
        minBidStep = this.minBidStep,
        highestBid = this.highestBid?.toDomain(),
        createdAt = OffsetDateTime.parse(this.createdAt),
        expirationDate = OffsetDateTime.parse(this.expirationDate),
        status = LotStatus.valueOf(this.status)
    )
}