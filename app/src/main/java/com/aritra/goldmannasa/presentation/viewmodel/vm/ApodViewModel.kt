package com.aritra.goldmannasa.presentation.viewmodel.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aritra.goldmannasa.data.local.entities.APODEntity
import com.aritra.goldmannasa.data.remote.network.utils.Resource
import com.aritra.goldmannasa.domain.usecase.ApodUsecase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ApodViewModel(private val apodUsecase: ApodUsecase) : ViewModel() {

    private var _apod:MutableLiveData<Resource<APODEntity>> = MutableLiveData()
    val apod = _apod




    fun getLatestAPOD() {
        viewModelScope.launch(Dispatchers.IO) {
            _apod.postValue(Resource.loading(null))
            val response = apodUsecase.getLatestAPOD()
            _apod.postValue(response)
        }
    }

    fun getDatedAPOD(date:String){
        viewModelScope.launch(Dispatchers.IO) {
            _apod.postValue(Resource.loading(null))
            val response = apodUsecase.getDatedAPOD(date = date)
            _apod.postValue(response)
        }
    }

    fun saveAPODToFavorites(apodEntity: APODEntity){
        viewModelScope.launch(Dispatchers.IO) {
            _apod.postValue(Resource.loading(null))
            val updateResponse = apodUsecase.saveAPODToFavorites(apodEntity)
            val updatedAPOD = apodUsecase.getDatedAPOD(apodEntity.date)
            _apod.postValue(updatedAPOD)
        }
    }
}