package com.ssafy.finalpjt

import androidx.lifecycle.ViewModel
import com.ssafy.finalpjt.database.repository.UserRepository

class MainViewModel : ViewModel() {
    var userRepository= UserRepository.get()



}