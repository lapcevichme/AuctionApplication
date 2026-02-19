package com.lapcevichme.auctionapplication.data.repository

import com.lapcevichme.auctionapplication.domain.model.CreateLotParam
import com.lapcevichme.auctionapplication.domain.model.Lot
import com.lapcevichme.auctionapplication.domain.model.LotSummary
import com.lapcevichme.auctionapplication.domain.repository.LotRepository
import io.ktor.client.HttpClient

class LotRepositoryImpl(
    private val httpClient: HttpClient
) : LotRepository {
    override suspend fun getLots(): Result<List<LotSummary>> {
        TODO("Not yet implemented")
    }

    override suspend fun getLotById(id: String): Result<Lot> {
        TODO("Not yet implemented")
    }

    override suspend fun createLot(param: CreateLotParam): Result<Lot> {
        TODO("Not yet implemented")
    }

    override suspend fun disableLot(id: String): Result<Unit> {
        TODO("Not yet implemented")
    }

}