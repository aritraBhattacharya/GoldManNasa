package com.aritra.goldmannasa

import android.app.Application
import com.aritra.goldmannasa.di.components.AppComponent
import com.aritra.goldmannasa.di.components.DaggerAppComponent
import com.aritra.goldmannasa.di.modules.DBModule
import com.aritra.goldmannasa.di.modules.NetworkModule


class NasaApp : Application() {
    lateinit var appComponent: AppComponent
    override fun onCreate() {

        super.onCreate()
        appInstance = this
        appComponent = DaggerAppComponent.factory().create(
            this,
            this,
            NetworkModule(),
            DBModule()
        )
    }

    companion object{
        lateinit var appInstance: NasaApp
    }
}