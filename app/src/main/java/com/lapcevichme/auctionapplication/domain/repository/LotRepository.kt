package com.lapcevichme.auctionapplication.domain.repository

import com.lapcevichme.auctionapplication.domain.model.lot.CreateLotParam
import com.lapcevichme.auctionapplication.domain.model.lot.Lot
import com.lapcevichme.auctionapplication.domain.model.lot.LotSummary

interface LotRepository {
    suspend fun getLots() : Result<List<LotSummary>>
    suspend fun getLotById(id: String) : Result<Lot>
    suspend fun createLot(param: CreateLotParam) : Result<Lot>
    suspend fun disableLot(id : String) : Result<Unit>
}