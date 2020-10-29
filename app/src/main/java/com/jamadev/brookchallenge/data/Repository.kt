package com.jamadev.brookchallenge.data

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.Bucket
import com.google.android.gms.fitness.data.DataPoint
import com.google.android.gms.fitness.data.DataSource
import com.google.android.gms.fitness.data.HealthDataTypes.TYPE_BLOOD_PRESSURE
import com.google.android.gms.fitness.data.HealthFields.*
import com.google.android.gms.fitness.request.DataReadRequest
import com.jamadev.brookchallenge.base.Constants.DAYS_OF_DATA
import com.jamadev.brookchallenge.base.Constants.GOOGLE_FIT_PERMISSIONS_REQUEST_CODE
import com.jamadev.brookchallenge.base.Constants.fitnessOptions
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.coroutines.suspendCoroutine

private const val TAG = "Repository"

class Repository(private val context: Context) : RepositoryContract {

    private val account = GoogleSignIn.getAccountForExtension(context, fitnessOptions)
    private val bloodPressureSource = DataSource.Builder()
        .setDataType(TYPE_BLOOD_PRESSURE).setType(DataSource.TYPE_RAW)
        .build()

    init {
        Fitness.getRecordingClient(context, account)
            .subscribe(bloodPressureSource)
            .addOnSuccessListener {
                Log.d(TAG, "Yes we can")
                createBloodPressureRecord(76f, 115f)
            }
    }

    override fun hasPermission(): Boolean = GoogleSignIn.hasPermissions(
        account, fitnessOptions
    )

    override fun requestPermission() {
        if (context is Activity)
            GoogleSignIn.requestPermissions(
                context, GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                account, fitnessOptions
            )
    }

    override suspend fun getBloodPressureData(): List<Bucket> {
        val calendar = Calendar.getInstance()
        val endTime = calendar.timeInMillis
        calendar.add(Calendar.DATE, DAYS_OF_DATA)
        val startTime = calendar.timeInMillis

        val readRequest: DataReadRequest = DataReadRequest.Builder()
            .aggregate(bloodPressureSource)
            .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
            .bucketByTime(1, TimeUnit.DAYS)
            .build()

        return suspendCoroutine {
            Fitness.getHistoryClient(context, account)
                .readData(readRequest)
                .addOnSuccessListener { response ->
                    // Use response data here
                    for (bucket in response.buckets) {
                        //Log.d(TAG, bucket.toString())
                        //Log.d(TAG, bucket.dataSets.size.toString())
                        //Log.d(TAG, bucket.dataSets[0].dataPoints.size.toString())
                    }
                    Log.d(TAG, response.dataSets.toString())
                    Log.d(TAG, "OnSuccess()")
                    it.resumeWith(Result.success(response.buckets))

                }
                .addOnFailureListener { e ->
                    Log.d(TAG, "OnFailure()", e)
                    it.resumeWith(Result.failure(e))
                }
        }
    }

    override fun createBloodPressureRecord(systolic: Float, diastolic: Float) {
        val bloodPressure = DataPoint.builder(bloodPressureSource)
            .setTimestamp(Calendar.getInstance().timeInMillis, TimeUnit.MILLISECONDS)
            .setField(FIELD_BLOOD_PRESSURE_SYSTOLIC, systolic)
            .setField(FIELD_BLOOD_PRESSURE_DIASTOLIC, diastolic)
            .setField(FIELD_BODY_POSITION, BODY_POSITION_SITTING)
            .setField(
                FIELD_BLOOD_PRESSURE_MEASUREMENT_LOCATION,
                BLOOD_PRESSURE_MEASUREMENT_LOCATION_LEFT_UPPER_ARM
            )
            .build()

        Log.d(TAG,bloodPressure.getValue(FIELD_BLOOD_PRESSURE_SYSTOLIC).toString())
        Log.d(TAG, "Yes")
    }

    override fun onDestroy() {
        Fitness.getRecordingClient(context, account)
            .unsubscribe(bloodPressureSource)
            .addOnSuccessListener {
                Log.i(TAG, "Successfully unsubscribed.")
            }
            .addOnFailureListener {
                // Subscription not removed
                Log.i(TAG, "Failed to unsubscribe. $it")
            }
    }


}