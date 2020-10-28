package com.jamadev.brookchallenge.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.HealthDataTypes
import com.google.android.gms.fitness.request.DataReadRequest
import java.util.*
import java.util.concurrent.TimeUnit

private const val TAG = "SplashActivity"

class SplashActivity : AppCompatActivity() {
    //In this module we could provide some loading logic

    private lateinit var fitnessOptions : FitnessOptions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fitnessOptions = FitnessOptions.builder()
            .addDataType(HealthDataTypes.TYPE_BLOOD_PRESSURE, FitnessOptions.ACCESS_READ)
            .addDataType(HealthDataTypes.TYPE_BLOOD_PRESSURE, FitnessOptions.ACCESS_WRITE)
            .addDataType(DataType.TYPE_WEIGHT, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.TYPE_WEIGHT, FitnessOptions.ACCESS_WRITE)
            .build()

        val account = GoogleSignIn.getAccountForExtension(this, fitnessOptions)

        if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    this,
                    1021,
                    account,
                    fitnessOptions)
        }else {
            accessGoogleFit();
        }

            //.addDataType(DataType., )
        //val intent = Intent(this, MainActivity::class.java)
        //startActivity(intent)
        //finishAffinity()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 1021) {
                accessGoogleFit()
            }
        }
    }

    private fun accessGoogleFit() {
        val cal: Calendar = Calendar.getInstance()
        cal.setTime(Date())
        val endTime: Long = cal.getTimeInMillis()
        cal.add(Calendar.YEAR, -1)
        val startTime: Long = cal.getTimeInMillis()
        val readRequest: DataReadRequest = DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .bucketByTime(1, TimeUnit.DAYS)
                .build()
        val account = GoogleSignIn
                .getAccountForExtension(this, fitnessOptions)
        Fitness.getHistoryClient(this, account)
                .readData(readRequest)
                .addOnSuccessListener { response ->
                    // Use response data here
                    Log.d(TAG, "OnSuccess()")
                }
                .addOnFailureListener { e -> Log.d(TAG, "OnFailure()", e) }
    }

}