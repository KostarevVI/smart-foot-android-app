package com.example.smartfootapp.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.smartfootapp.db.entity.Exercise
import com.example.smartfootapp.db.entity.UserExercise

@Dao
interface ExerciseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExercise(newExercise: Exercise): Long

    @Query("SELECT * FROM Exercise")
    fun getAllExercises(): List<Exercise>

    @Query("SELECT * FROM UserExercise WHERE userId = :userId")
    fun getAllUserExercisesByUserId(userId: Long): List<UserExercise>
}