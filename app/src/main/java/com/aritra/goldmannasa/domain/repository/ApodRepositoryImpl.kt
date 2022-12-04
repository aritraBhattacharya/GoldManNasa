package com.aritra.goldmannasa.domain.repository

import android.util.Log
import com.aritra.goldmannasa.data.local.db.ApodDao
import com.aritra.goldmannasa.data.local.entities.APODEntity
import com.aritra.goldmannasa.data.remote.NetworkUtils
import com.aritra.goldmannasa.data.remote.dtos.APODDto
import com.aritra.goldmannasa.data.remote.network.NasaApi
import com.aritra.goldmannasa.data.remote.network.utils.Resource
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
            /*check if data is available in local db
            * if yes --> return the same as entity format
            * else make the network call
            * if network call is successful --> save data in DB --> return the same
            * is network call fails return error*/

            //Log.i(TAG,"is network working ${networkUtils.getConnectivityStatus()}")
            val today = getTodaysDate()
            //Log.i(TAG, "getLatestAPOD for date: $today")
            val dbResponse = apodDao.getLocalAPODForDate(today)
            if (dbResponse.isNullOrEmpty()) {
                //Log.i(TAG, "getLatestAPOD db response if empty: making network call")
                val apiResponse = nasaApi.getLatestAPOD()
                if (apiResponse.isSuccessful) {
                    //Log.i(TAG, "getLatestAPOD network call is successful")
                    val apodDto = apiResponse.body()
                    val equivalentEntity = apodDto?.toEntity()
                    equivalentEntity?.let {
                        //Log.i(TAG, "getLatestAPOD saving network result to local DB")
                        apodDao.insertAPOD(it)
                        Resource.success(it)
                    } ?: Resource.error(apiResponse.code().toString(), null)
                } else {
                    //Log.i(TAG, "getLatestAPOD network call failed")
                    Resource.error(apiResponse.code().toString(), null)
                }
            } else {
                //Log.i(TAG, "getLatestAPOD returning data from db")
                return Resource.success(dbResponse[0])
            }
        } catch (e: Exception) {
            Resource.error("" + e.message, null)
        }
    }

    override suspend fun getDatedAPOD(date: String): Resource<APODEntity> {
        return try {
            //Log.i(TAG, "getDatedAPOD for date: $date")
            val dbResponse = apodDao.getLocalAPODForDate(date)
            if (dbResponse.isNullOrEmpty()) {
                //Log.i(TAG, "getDatedAPOD db response if empty: making network call")
                val apiResponse = nasaApi.getDatedAPOD(date = date)
                if (apiResponse.isSuccessful) {
                    //Log.i(TAG, "getDatedAPOD network call is successful")
                    val apodDto = apiResponse.body()
                    val equivalentEntity = apodDto?.toEntity()
                    equivalentEntity?.let {
                        //Log.i(TAG, "getDatedAPOD saving network result to local DB")
                        apodDao.insertAPOD(it)
                        Resource.success(it)
                    } ?: Resource.error(apiResponse.code().toString(), null)
                } else {
                    //Log.i(TAG, "getDatedAPOD network call failed")
                    Resource.error(apiResponse.code().toString(), null)
                }
            } else {
                //Log.i(TAG, "getDatedAPOD returning data from db")
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
            Resource.error(msg = "Failed to mark as favourite", data = false)
        }
    }
}