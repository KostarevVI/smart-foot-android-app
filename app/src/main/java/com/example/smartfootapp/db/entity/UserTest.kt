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
            entity = Test::class,
            parentColumns = ["id"],
            childColumns = ["testId"]
        )],
    primaryKeys = ["userId", "testId"],
    tableName = "UserTest"
)
data class UserTest(
    val userId: Long,
    val testId: Long,
    val completedAt: String,
    val results: String,
    val settings: String,
)