package com.jamadev.brookchallenge.base

import com.google.android.gms.fitness.data.DataPoint
import com.google.android.gms.fitness.data.HealthDataTypes
import com.google.android.gms.fitness.data.HealthFields
import com.jamadev.brookchallenge.base.Constants.DATE_FORMAT
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

fun DataPoint.getStartTimeString(): String = DateFormat.getTimeInstance()
    .format(this.getStartTime(TimeUnit.MILLISECONDS))

fun DataPoint.getSystolicPressure():Int? {
    if(this.dataType == HealthDataTypes.TYPE_BLOOD_PRESSURE ){
        return this.getValue(HealthFields.FIELD_BLOOD_PRESSURE_SYSTOLIC).asFloat().toInt()
    }
    return null
}

fun DataPoint.getDiastolicPressure():Int? {
    if(this.dataType == HealthDataTypes.TYPE_BLOOD_PRESSURE ){
        return this.getValue(HealthFields.FIELD_BLOOD_PRESSURE_DIASTOLIC).asFloat().toInt()
    }
    return null
}

fun DataPoint.getDateString():String{
    val formatter = SimpleDateFormat(DATE_FORMAT)
    val calendar: Calendar = Calendar.getInstance()
    calendar.timeInMillis = this.getTimestamp(TimeUnit.MILLISECONDS)
    return formatter.format(calendar.time)
}