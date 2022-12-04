package com.aritra.goldmannasa.di.components

import android.content.Context
import com.aritra.goldmannasa.NasaApp
import com.aritra.goldmannasa.di.modules.ContainerModule
import com.aritra.goldmannasa.di.modules.DBModule
import com.aritra.goldmannasa.di.modules.NetworkModule
import com.aritra.goldmannasa.presentation.ui.activities.ContainerActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    NetworkModule::class,
    ContainerModule::class,
    DBModule::class])
interface AppComponent {

    fun inject(containerActivity: ContainerActivity)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance nasaApp: NasaApp, // bind instance as application instance is a run time value
            @BindsInstance appContext: Context, // bind instance as application instance is a run time value
            networkModule: NetworkModule,
            dbModule: DBModule
        ): AppComponent
    }

}