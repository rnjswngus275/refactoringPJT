package com.ssafy.finalpjt

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.finalpjt.database.dto.Shop
import com.ssafy.finalpjt.database.dto.User
import com.ssafy.finalpjt.database.repository.ShopRepository
import com.ssafy.finalpjt.database.repository.UserRepository
import kotlinx.coroutines.launch

class FragmentShopViewModel(var id: Long) : ViewModel() {

    private val userRepository = UserRepository.get()
    private val shopRepository = ShopRepository.get()

    val shopList: LiveData<ArrayList<Shop>> = shopRepository.getShop()

    fun insertShop(item: Shop) {
        viewModelScope.launch {
            shopRepository.insertShop(item)
        }
    }

    fun deleteShop(item: Shop) {
        viewModelScope.launch {
            shopRepository.deleteShop(item)
        }
    }

    fun updateUser(price: Int) {
        viewModelScope.launch {
            val user = getUser()
            user.Point -= price
            userRepository.updateUser(user)
        }
    }

    fun getUser() : User {
        return userRepository.getUser(id)
    }


}