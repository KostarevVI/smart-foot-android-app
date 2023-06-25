package com.example.smartfootapp

import android.app.Application
import android.content.Context
import com.example.smartfootapp.di.AppComponent
import com.example.smartfootapp.di.DaggerAppComponent
import com.example.smartfootapp.di.RoomDatabaseModule

class App : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent =
            DaggerAppComponent.builder().roomDatabaseModule(RoomDatabaseModule(this)).build()
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> appComponent
        else -> this.applicationContext.appComponent
    }
