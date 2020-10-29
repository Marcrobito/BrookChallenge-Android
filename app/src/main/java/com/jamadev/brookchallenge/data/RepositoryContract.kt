package com.jamadev.brookchallenge.data

import com.google.android.gms.fitness.data.Bucket

interface RepositoryContract {
    fun hasPermission():Boolean
    fun requestPermission()
    suspend fun getBloodPressureData(): List<Bucket>
    fun createBloodPressureRecord(systolic: Float, diastolic: Float)
    fun onDestroy()
}