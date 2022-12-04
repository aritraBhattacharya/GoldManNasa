package com.aritra.goldmannasa.domain.usecase

import com.aritra.goldmannasa.data.local.entities.APODEntity
import com.aritra.goldmannasa.data.remote.network.utils.Resource
import com.aritra.goldmannasa.domain.repository.ApodRepository
import javax.inject.Inject

class ApodUsecaseImpl @Inject constructor(private val repository: ApodRepository) : ApodUsecase {
    override suspend fun getLatestAPOD(): Resource<APODEntity> {
        return repository.getLatestAPOD()
    }

    override suspend fun getDatedAPOD(date: String): Resource<APODEntity> {
        return repository.getDatedAPOD(date)
    }

    override suspend fun saveAPODToFavorites(apod: APODEntity):Resource<Boolean> {
        return repository.saveAPODToFavorites(apod)
    }
}