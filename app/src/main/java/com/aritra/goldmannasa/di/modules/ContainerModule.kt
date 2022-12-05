package com.aritra.goldmannasa.di.modules

import com.aritra.goldmannasa.domain.repository.ApodRepository
import com.aritra.goldmannasa.domain.repository.ApodRepositoryImpl
import com.aritra.goldmannasa.domain.usecase.ApodUsecase
import com.aritra.goldmannasa.domain.usecase.ApodUsecaseImpl
import dagger.Binds
import dagger.Module

@Module
abstract class ContainerModule {

    @Binds
    abstract fun getSearchRepository(repoImpl: ApodRepositoryImpl): ApodRepository

    @Binds
    abstract fun getSearchUseCase(usecaseImpl: ApodUsecaseImpl): ApodUsecase
}