package com.jamadev.brookchallenge

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])

interface AppComponent {

}