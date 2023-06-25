package com.example.smartfootapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.smartfootapp.db.dao.ExerciseDao
import com.example.smartfootapp.db.dao.TestDao
import com.example.smartfootapp.db.dao.UserDao
import com.example.smartfootapp.db.entity.Exercise
import com.example.smartfootapp.db.entity.Test
import com.example.smartfootapp.db.entity.User
import com.example.smartfootapp.db.entity.UserExercise
import com.example.smartfootapp.db.entity.UserTest

@Database(
    version = 1,
    entities = [
        User::class,
        Test::class,
        Exercise::class,
        UserExercise::class,
        UserTest::class,
    ]
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun userDao(): UserDao

    abstract fun testDao(): TestDao

    abstract fun exerciseDao(): ExerciseDao
}