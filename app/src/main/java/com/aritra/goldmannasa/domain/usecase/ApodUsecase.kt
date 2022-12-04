package com.aritra.goldmannasa.domain.usecase

import com.aritra.goldmannasa.data.local.entities.APODEntity
import com.aritra.goldmannasa.data.remote.network.utils.Resource

interface ApodUsecase {

    suspend fun getLatestAPOD(): Resource<APODEntity>
    suspend fun getDatedAPOD(date:String): Resource<APODEntity>
    suspend fun saveAPODToFavorites(apod: APODEntity): Resource<Boolean>
}