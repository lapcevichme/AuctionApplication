package com.lapcevichme.auctionapplication.domain.repository

import com.lapcevichme.auctionapplication.domain.model.Bid
import com.lapcevichme.auctionapplication.domain.model.BidSummary
import com.lapcevichme.auctionapplication.domain.model.MakeBidParam

interface BidRepository {
    suspend fun getBids(lotId : String) : Result<List<BidSummary>>
    suspend fun getBidById(lotId : String, bidId : String) : Result<Bid>
    suspend fun makeBid(lotId: String, param: MakeBidParam) : Result<Bid>
}