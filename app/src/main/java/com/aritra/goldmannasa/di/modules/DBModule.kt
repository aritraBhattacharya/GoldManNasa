package com.aritra.goldmannasa.di.modules

import android.content.Context
import com.aritra.goldmannasa.data.local.db.ApodDB
import dagger.Module
import dagger.Provides

@Module
class DBModule {

    @Provides
    fun provideDB(appContext: Context) = ApodDB.getDatabase(appContext)

    @Provides
    fun provideDao(apodDB: ApodDB) = apodDB.getApodDao()


}