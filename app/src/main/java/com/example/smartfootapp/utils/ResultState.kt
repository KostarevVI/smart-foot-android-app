package com.example.smartfootapp.utils

sealed class ResultState<T>(val data: T?, val message: String) {
    companion object {
        fun <T> success(data: T?, message: String): Success<T> = Success(data, message)

        fun <T> error(data: T?, message: String): Error<T> = Error(data, message)

        fun <T> loading(data: T?, message: String): Loading<T> = Loading(data, message)
    }
}

class Success<T>(data: T?, message: String) : ResultState<T>(data, message)

class Error<T>(data: T?, message: String) : ResultState<T>(data, message)

class Loading<T>(data: T?, message: String) : ResultState<T>(data, message)

