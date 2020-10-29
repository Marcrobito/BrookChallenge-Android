package com.jamadev.brookchallenge.base

import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.HealthDataTypes

object Constants {

    const val GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 8932
    val fitnessOptions: FitnessOptions = FitnessOptions.builder()
        .addDataType(HealthDataTypes.TYPE_BLOOD_PRESSURE, FitnessOptions.ACCESS_READ)
        .addDataType(HealthDataTypes.TYPE_BLOOD_PRESSURE, FitnessOptions.ACCESS_WRITE).build()

}