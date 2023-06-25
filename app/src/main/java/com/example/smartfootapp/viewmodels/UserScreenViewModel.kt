package com.example.smartfootapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartfootapp.base.BaseViewModel
import com.example.smartfootapp.db.DatabaseRepository
import com.example.smartfootapp.db.entity.User
import com.example.smartfootapp.utils.ResultState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserScreenViewModel @Inject constructor(private val dbRepository: DatabaseRepository) :
    ViewModel() {

    init {
        getUsers()
    }

    private var _users: MutableStateFlow<List<User>> = MutableStateFlow(emptyList())
    val users: StateFlow<List<User>>
        get() = _users.asStateFlow()

    private fun getUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            dbRepository.getUsersFlow().collect {
                _users.value = it
            }
        }
    }

    fun addUser(newUser: User): LiveData<ResultState<Any?>> {
        val mutableLiveData = MutableLiveData<ResultState<Any?>>()

        viewModelScope.launch {
            dbRepository.insertUser(newUser).collect {
                mutableLiveData.postValue(it)
            }
        }

        return mutableLiveData
    }

    fun deleteUserById(userId: Long): LiveData<ResultState<Any?>> {
        val mutableLiveData = MutableLiveData<ResultState<Any?>>()

        viewModelScope.launch {
            dbRepository.deleteUserById(userId).collect {
                mutableLiveData.postValue(it)
            }
        }

        return mutableLiveData

    }
}