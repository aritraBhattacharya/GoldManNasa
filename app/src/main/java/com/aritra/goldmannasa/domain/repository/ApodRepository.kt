package com.aritra.goldmannasa.domain.repository

import com.aritra.goldmannasa.data.local.entities.APODEntity
import com.aritra.goldmannasa.data.remote.network.utils.Resource

interface ApodRepository {
    suspend fun getLatestAPOD(): Resource<APODEntity>
    suspend fun getDatedAPOD(date: String): Resource<APODEntity>
    suspend fun saveAPODToFavorites(apod: APODEntity): Resource<Boolean>
}