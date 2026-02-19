package com.lapcevichme.auctionapplication.domain.usecase.lots

import com.lapcevichme.auctionapplication.domain.model.PagedData
import com.lapcevichme.auctionapplication.domain.model.lot.LotSummary
import com.lapcevichme.auctionapplication.domain.repository.LotRepository

class GetLotsUseCase(val repository: LotRepository) {
    suspend operator fun invoke(page: Int, size: Int) : Result<PagedData<LotSummary>> {
        return repository.getLots(page, size)
    }
}