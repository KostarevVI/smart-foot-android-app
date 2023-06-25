package com.example.smartfootapp.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.smartfootapp.db.entity.Test

@Dao
interface TestDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTest(newTest: Test): Long

    @Query("SELECT * FROM Test")
    fun getAllTests(): List<Test>
}