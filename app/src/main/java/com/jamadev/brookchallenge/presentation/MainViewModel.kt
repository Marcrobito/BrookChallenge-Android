package com.jamadev.brookchallenge.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jamadev.brookchallenge.data.RepositoryContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "MainViewModel"

class MainViewModel : ViewModel() {

    private val _hasGoogleFitPermission = MutableLiveData<Boolean>()
    val hasGoogleFitPermission: LiveData<Boolean> get() = _hasGoogleFitPermission

    private var repository: RepositoryContract? = null

    fun setRepository(repository: RepositoryContract?) {
        this.repository = repository
        _hasGoogleFitPermission.value = repository?.hasPermission() ?: false
    }

    fun userAskForPermission() {
        repository?.requestPermission()
    }

    fun permissionHasGranted(){
        viewModelScope.launch {
            val data = withContext(Dispatchers.IO){
                repository?.getBloodPressureData()
            }
        }
    }

    fun storeBloodPressureData(systolic: Float, diastolic: Float) {
        repository?.createBloodPressureRecord(systolic, diastolic)
    }

}