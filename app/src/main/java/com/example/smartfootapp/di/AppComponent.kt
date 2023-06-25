package com.example.smartfootapp.di

import android.app.Application
import com.example.smartfootapp.BleActivity
import com.example.smartfootapp.MainActivity
import com.example.smartfootapp.di.vm.ViewModelFactory
import com.example.smartfootapp.di.vm.ViewModelModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RoomDatabaseModule::class, ViewModelModule::class])
interface AppComponent {
    fun viewModelFactory(): ViewModelFactory

    fun inject(activity: MainActivity)
    fun inject(activity: BleActivity)
}
