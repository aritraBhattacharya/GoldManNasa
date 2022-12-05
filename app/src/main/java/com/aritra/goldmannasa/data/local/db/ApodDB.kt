package com.aritra.goldmannasa.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aritra.goldmannasa.data.local.entities.APODEntity

@Database(
    entities = [APODEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ApodDB : RoomDatabase() {
    abstract fun getApodDao(): ApodDao

    companion object {
        @Volatile
        private var INSTANCE: ApodDB? = null

        fun getDatabase(context: Context): ApodDB {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val roomBuilder = Room.databaseBuilder(
                    context.applicationContext,
                    ApodDB::class.java,
                    "apod_database"
                )
                val instance = roomBuilder.build()
                INSTANCE = instance
                instance
            }
        }
    }
}