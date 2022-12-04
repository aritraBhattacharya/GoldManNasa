package com.aritra.goldmannasa.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aritra.goldmannasa.data.local.entities.APODEntity

@Dao
interface ApodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAPOD(apodEntity: APODEntity)

    @Query("SELECT * FROM apod_table")
    suspend fun getAllLocalAPOD(): List<APODEntity>

    @Query("SELECT * FROM apod_table where date= :date")
    suspend fun getLocalAPODForDate(date:String): List<APODEntity>

    @Query("UPDATE apod_table SET isFavourite=:isFavourite where date= :date")
    suspend fun makeAPODFavourite(date:String,isFavourite:Boolean)
}