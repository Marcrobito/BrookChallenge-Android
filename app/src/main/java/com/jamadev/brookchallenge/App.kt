package com.jamadev.brookchallenge

import android.app.Application
import android.content.res.Resources
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.jamadev.brookchallenge.base.Constants

class App:Application() {
    companion object{
        lateinit var resAccess: Resources
    }

    override fun onCreate() {
        super.onCreate()
        resAccess = resources
        val account = GoogleSignIn.getAccountForExtension(this, Constants.fitnessOptions)
        if (GoogleSignIn.hasPermissions(account, Constants.fitnessOptions)){
            /*val i = Intent(this, Service::class.java)
                startService(i)*/
        }

    }


}