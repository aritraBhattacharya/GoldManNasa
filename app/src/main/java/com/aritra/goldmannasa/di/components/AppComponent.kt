package com.aritra.goldmannasa.di.components

import android.content.Context
import com.aritra.goldmannasa.NasaApp
import com.aritra.goldmannasa.di.modules.DBModule
import com.aritra.goldmannasa.di.modules.NetworkModule
import com.aritra.goldmannasa.di.subcomponents.APODSubComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    NetworkModule::class,
    DBModule::class])
interface AppComponent {

    fun getAPODSubComponentFactory(): APODSubComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance nasaApp: NasaApp,
            @BindsInstance appContext: Context,
            networkModule: NetworkModule,
            dbModule: DBModule,
        ): AppComponent
    }

}