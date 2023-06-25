package com.example.smartfootapp.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "User"
)
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val dateOfBirth: String,
    val height: Float,
    val weight: Float,
)