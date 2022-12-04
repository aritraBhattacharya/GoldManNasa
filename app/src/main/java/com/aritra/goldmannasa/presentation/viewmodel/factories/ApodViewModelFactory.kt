package com.aritra.goldmannasa.presentation.viewmodel.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aritra.goldmannasa.domain.usecase.ApodUsecase
import com.aritra.goldmannasa.presentation.viewmodel.vm.ApodViewModel
import javax.inject.Inject

class ApodViewModelFactory @Inject constructor(val apodUsecase: ApodUsecase) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ApodViewModel(apodUsecase) as T
    }
}