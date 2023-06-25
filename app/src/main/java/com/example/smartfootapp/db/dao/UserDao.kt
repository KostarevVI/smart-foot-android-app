package com.example.smartfootapp.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.smartfootapp.db.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(newUser: User): Long

    @Query("DELETE FROM User WHERE id = :id")
    fun deleteUserById(id: Long)

    @Query("SELECT * FROM User")
    fun getUsersFlow(): Flow<List<User>>
}