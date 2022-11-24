package com.ssafy.finalpjt.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.finalpjt.database.dto.User
import com.ssafy.finalpjt.database.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel : ViewModel() {

    private var userRepository = UserRepository.get()

    var userList = userRepository.getAllUser()

    fun insertUser(user: User) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                userRepository.insertUser(user)
            }
        }
    }
}