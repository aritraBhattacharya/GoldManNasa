package com.aritra.goldmannasa.domain.repository

import com.aritra.goldmannasa.data.local.db.ApodDao
import com.aritra.goldmannasa.data.local.entities.APODEntity
import com.aritra.goldmannasa.data.remote.NetworkUtils
import com.aritra.goldmannasa.data.remote.network.NasaApi
import com.aritra.goldmannasa.data.remote.network.utils.Resource
import com.aritra.goldmannasa.utils.FAILED_TO_MARK_AS_FAV
import com.aritra.goldmannasa.utils.NO_NETWORK_MESSAGE
import com.aritra.goldmannasa.utils.getTodaysDate
import com.aritra.goldmannasa.utils.toEntity
import javax.inject.Inject

private const val TAG = "ApodRepositoryImpl"

class ApodRepositoryImpl @Inject constructor(
    private val nasaApi: NasaApi,
    private val apodDao: ApodDao,
    private val networkUtils: NetworkUtils,
) : ApodRepository {
    override suspend fun getLatestAPOD(): Resource<APODEntity> {
        return try {
            val today = getTodaysDate()
            // call getDatedAPOD for today's date
            getDatedAPOD(today)
        } catch (e: Exception) {
            Resource.error(e.message.toString(), null)
        }
    }

    override suspend fun getDatedAPOD(date: String): Resource<APODEntity> {
        /*check if data is available in local db
           * if yes --> return the same as entity format
           * else check if network is available --> if not then send error with no network message
           * else make the network call
           * if network call is successful --> save data in DB --> return the same
           * is network call fails return error*/
        return try {
            val dbResponse = apodDao.getLocalAPODForDate(date)
            if (dbResponse.isNullOrEmpty()) {
                if (networkUtils.getConnectivityStatus()) {
                    val apiResponse = nasaApi.getDatedAPOD(date = date)
                    if (apiResponse.isSuccessful) {
                        val apodDto = apiResponse.body()
                        val equivalentEntity = apodDto?.toEntity()
                        equivalentEntity?.let {
                            apodDao.insertAPOD(it)
                            Resource.success(it)
                        } ?: Resource.error(apiResponse.code().toString(), null)
                    } else {
                        Resource.error(apiResponse.code().toString(), null)
                    }
                } else {
                    Resource.error(NO_NETWORK_MESSAGE, null)
                }
            } else {
                return Resource.success(dbResponse[0])
            }
        } catch (e: Exception) {
            Resource.error(e.message.toString(), null)
        }
    }

    override suspend fun saveAPODToFavorites(apod: APODEntity): Resource<Boolean> {
        return try {
            apodDao.makeAPODFavourite(apod.date, !apod.isFavourite)
            Resource.success(true)
        } catch (e: Exception) {
            Resource.error(msg = FAILED_TO_MARK_AS_FAV, data = false)
        }
    }
}