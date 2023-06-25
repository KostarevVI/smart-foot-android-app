package com.example.smartfootapp.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.smartfootapp.db.AppDataBase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomDatabaseModule(private var application: Application) {
    private lateinit var database: AppDataBase

    @Singleton
    @Provides
    fun providesRoomDatabase(): AppDataBase {
        database = Room.databaseBuilder(application, AppDataBase::class.java, "SmartFootDatabase")
            .fallbackToDestructiveMigration()
            .build()
        return database
    }

    @Singleton
    @Provides
    fun provideUserDao(database: AppDataBase) = database.userDao()

    @Singleton
    @Provides
    fun provideExerciseDao(database: AppDataBase) = database.exerciseDao()

    @Singleton
    @Provides
    fun provideTestDao(database: AppDataBase) = database.testDao()
}