package com.jamadev.brookchallenge.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.fitness.data.DataPoint
import com.google.android.gms.fitness.data.DataSource
import com.google.android.gms.fitness.data.HealthDataTypes
import com.google.android.gms.fitness.data.HealthFields
import com.jamadev.brookchallenge.base.Constants
import com.jamadev.brookchallenge.data.RepositoryContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

private const val TAG = "MainViewModel"

class MainViewModel : ViewModel() {

    private val _hasGoogleFitPermission = MutableLiveData<Boolean>()
    val hasGoogleFitPermission: LiveData<Boolean> get() = _hasGoogleFitPermission

    private val _dataPoints = MutableLiveData<List<DataPoint>>()
    val dataPoints: LiveData<List<DataPoint>> get() = _dataPoints

    private val _lastTimeUpdated = MutableLiveData<String>()
    val lastTimeUpdated:LiveData<String> get() = _lastTimeUpdated

    private val formatter = SimpleDateFormat(Constants.LAST_SYNC_DATE_FORMAT)
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
            _dataPoints.value = generateRandomData()
            _lastTimeUpdated.value = "Last Google Fit Sync: ${getLastTimeUpDated()}"
            Log.d(TAG,data.toString())
        }
    }

    fun storeBloodPressureData(systolic: Float, diastolic: Float) {
        repository?.createBloodPressureRecord(systolic, diastolic)
    }

    private fun getLastTimeUpDated() = formatter.format( Calendar.getInstance().timeInMillis)


    private fun generateRandomData():List<DataPoint>{
        val data = mutableListOf<DataPoint>()
        for(i in 0..15){
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DATE, -i)
            val millis = calendar.timeInMillis

            data.add(generateRandomData(millis))
            Log.d(TAG,data[i].getTimestamp(TimeUnit.MILLISECONDS).toString())
        }
        return data
    }

    private fun generateRandomData(millis:Long):DataPoint{
        Log.d(TAG, millis.toString())
        val diastolic = (65..90).random().toFloat()
        val systolic = (110..135).random().toFloat()
        val bloodPressureSource = DataSource.Builder()
            .setDataType(HealthDataTypes.TYPE_BLOOD_PRESSURE).setType(DataSource.TYPE_RAW)
            .build()
        return DataPoint.builder(bloodPressureSource)
            .setTimestamp(millis, TimeUnit.MILLISECONDS)
            .setField(HealthFields.FIELD_BLOOD_PRESSURE_SYSTOLIC, systolic)
            .setField(HealthFields.FIELD_BLOOD_PRESSURE_DIASTOLIC, diastolic)
            .setField(HealthFields.FIELD_BODY_POSITION, HealthFields.BODY_POSITION_SITTING)
            .setField(
                HealthFields.FIELD_BLOOD_PRESSURE_MEASUREMENT_LOCATION,
                HealthFields.BLOOD_PRESSURE_MEASUREMENT_LOCATION_LEFT_UPPER_ARM
            ).build()

    }

}