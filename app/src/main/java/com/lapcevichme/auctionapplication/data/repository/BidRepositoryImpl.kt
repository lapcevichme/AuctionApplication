package com.lapcevichme.auctionapplication.data.repository

import com.lapcevichme.auctionapplication.domain.model.PagedData
import com.lapcevichme.auctionapplication.domain.model.bid.Bid
import com.lapcevichme.auctionapplication.domain.model.bid.BidSummary
import com.lapcevichme.auctionapplication.domain.model.bid.MakeBidParam
import com.lapcevichme.auctionapplication.domain.repository.BidRepository
import io.ktor.client.HttpClient

class BidRepositoryImpl(
    private val httpClient: HttpClient
) : BidRepository {
    override suspend fun getBids(lotId: String): Result<PagedData<BidSummary>> {
        TODO("Not yet implemented")
    }

    override suspend fun getBidById(
        lotId: String,
        bidId: String
    ): Result<Bid> {
        TODO("Not yet implemented")
    }

    override suspend fun makeBid(
        lotId: String,
        param: MakeBidParam
    ): Result<Bid> {
        TODO("Not yet implemented")
    }

}