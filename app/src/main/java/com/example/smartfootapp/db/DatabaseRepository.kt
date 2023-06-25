package com.example.smartfootapp.db

import android.util.Log
import com.example.smartfootapp.db.dao.UserDao
import com.example.smartfootapp.db.entity.User
import com.example.smartfootapp.utils.ResultState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseRepository @Inject constructor(private val userDao: UserDao) {

    fun getUsersFlow(): Flow<List<User>> {
        return userDao.getUsersFlow()
    }

    fun insertUser(newUser: User): Flow<ResultState<Any?>> = flow<ResultState<Any?>> {
        emit(ResultState.loading(null, "Добавление пользователя"))
        userDao.insertUser(newUser)
        emit(ResultState.success(null, "Пользователь добавлен"))

    }.catch { e ->
        Log.d(LOG_TAG, "insertUser: $e")
    }
        .flowOn(Dispatchers.IO)

    fun deleteUserById(userId: Long): Flow<ResultState<Any?>> = flow<ResultState<Any?>> {
        emit(ResultState.loading(null, "Добавление пользователя"))
        userDao.deleteUserById(userId)
        emit(ResultState.success(null, "Пользователь добавлен"))

    }.catch { e ->
        Log.d(LOG_TAG, "insertUser: $e")
    }
        .flowOn(Dispatchers.IO)

    companion object {
        private const val LOG_TAG = "DatabaseRepository"
    }
}