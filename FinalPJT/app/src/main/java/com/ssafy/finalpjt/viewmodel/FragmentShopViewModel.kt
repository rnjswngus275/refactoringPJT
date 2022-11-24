package com.ssafy.finalpjt.viewmodel

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.finalpjt.database.DatabaseApplicationClass
import com.ssafy.finalpjt.database.dto.Shop
import com.ssafy.finalpjt.database.dto.User
import com.ssafy.finalpjt.database.repository.ShopRepository
import com.ssafy.finalpjt.database.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentShopViewModel() : ViewModel() {

    private val userRepository = UserRepository.get()
    private val shopRepository = ShopRepository.get()
    private val sharedPreferencesUtil = DatabaseApplicationClass.sharedPreferencesUtil

    val shopList: LiveData<MutableList<Shop>> = shopRepository.getShop()
    var userName = sharedPreferencesUtil.getUserName()
    val user: LiveData<User> = userRepository.getUserByName(userName)

    fun insertShop(item: Shop) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                shopRepository.insertShop(item)
            }
        }
    }

    fun deleteShop(item: Shop) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                shopRepository.deleteShop(item)
            }
        }
    }

    fun updateUser(updatePoint: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                userRepository.updateUserPoint(updatePoint, userName)
            }
        }
    }
}