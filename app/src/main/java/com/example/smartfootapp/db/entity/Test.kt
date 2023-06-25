package com.example.smartfootapp.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "Test"
)
data class Test(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val payload: String,
)