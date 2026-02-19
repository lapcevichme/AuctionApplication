package com.lapcevichme.auctionapplication.data.repository

import com.lapcevichme.auctionapplication.domain.model.Bid
import com.lapcevichme.auctionapplication.domain.model.BidSummary
import com.lapcevichme.auctionapplication.domain.model.MakeBidParam
import com.lapcevichme.auctionapplication.domain.repository.BidRepository
import io.ktor.client.HttpClient

class BidRepositoryImpl(
    private val httpClient: HttpClient
) : BidRepository {
    override suspend fun getBids(lotId: String): Result<List<BidSummary>> {
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