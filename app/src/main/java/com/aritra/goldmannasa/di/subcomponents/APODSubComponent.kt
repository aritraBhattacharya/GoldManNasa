package com.aritra.goldmannasa.di.subcomponents

import com.aritra.goldmannasa.di.modules.ContainerModule
import com.aritra.goldmannasa.di.scopes.APODScope
import com.aritra.goldmannasa.presentation.ui.activities.ContainerActivity
import dagger.Subcomponent

@APODScope
@Subcomponent(modules = [ContainerModule::class])
interface APODSubComponent {

    fun inject(containerActivity: ContainerActivity)


    @Subcomponent.Factory
    interface Factory{
        fun create():APODSubComponent
    }
}