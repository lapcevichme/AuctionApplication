package com.lapcevichme.auctionapplication.domain.usecase.lots

import com.lapcevichme.auctionapplication.domain.model.lot.Lot
import com.lapcevichme.auctionapplication.domain.repository.LotRepository

class GetLotByIdUseCase(private val repository: LotRepository) {
    suspend operator fun invoke(id: String): Result<Lot> {
        return repository.getLotById(id)
    }
}