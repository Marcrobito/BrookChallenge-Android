package com.jamadev.brookchallenge

import android.app.Application
import javax.inject.Singleton


@Singleton
class App:Application() {

    private lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.builder()
            .build()
        /*val i = Intent(this, RandomMealService::class.java)
        startService(i)*/
    }

    fun getComponent() = component
}