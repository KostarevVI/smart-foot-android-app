package com.example.smartfootapp.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "Exercise"
)
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val data: String,
    val isCustom: Boolean,
)