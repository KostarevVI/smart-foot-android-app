package com.example.smartfootapp.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["userId"]
    ),
        ForeignKey(
            entity = Exercise::class,
            parentColumns = ["id"],
            childColumns = ["exerciseId"]
        )],
    primaryKeys = ["userId", "exerciseId"],
    tableName = "UserExercise"
)
data class UserExercise(
    val userId: Long,
    val exerciseId: Long,
    val completedAt: String,
    val results: String,
    val settings: String,
)