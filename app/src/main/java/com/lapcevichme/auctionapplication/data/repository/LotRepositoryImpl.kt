package com.lapcevichme.auctionapplication.data.repository

import com.lapcevichme.auctionapplication.data.remote.dto.PageResponseDto
import com.lapcevichme.auctionapplication.data.remote.dto.lot.LotDto
import com.lapcevichme.auctionapplication.data.remote.dto.lot.LotSummaryDto
import com.lapcevichme.auctionapplication.data.remote.dto.lot.toDomain
import com.lapcevichme.auctionapplication.data.remote.safeApiCall
import com.lapcevichme.auctionapplication.domain.model.PagedData
import com.lapcevichme.auctionapplication.domain.model.lot.CreateLotParam
import com.lapcevichme.auctionapplication.domain.model.lot.Lot
import com.lapcevichme.auctionapplication.domain.model.lot.LotSummary
import com.lapcevichme.auctionapplication.domain.repository.LotRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class LotRepositoryImpl(
    private val httpClient: HttpClient
) : LotRepository {
    override suspend fun getLots(page: Int, size: Int): Result<PagedData<LotSummary>> {
        return safeApiCall {
            val response = httpClient.get("lots") {
                parameter("page", page)
                parameter("size", size)
            }.body<PageResponseDto<LotSummaryDto>>()

            PagedData(
                items = response.content.map { it.toDomain() },
                isLastPage = response.last
            )
        }
    }

    override suspend fun getLotById(id: String): Result<Lot> {
        return safeApiCall {
            httpClient.get("lots/$id").body<LotDto>().toDomain()
        }
    }

    override suspend fun createLot(param: CreateLotParam): Result<Lot> {
        TODO("Not yet implemented")
    }

    override suspend fun disableLot(id: String): Result<Unit> {
        TODO("Not yet implemented")
    }

}