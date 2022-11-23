package com.ssafy.finalpjt

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.finalpjt.database.dto.Shop
import com.ssafy.finalpjt.database.dto.User
import com.ssafy.finalpjt.database.repository.ShopRepository
import com.ssafy.finalpjt.database.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "FragmentShopViewModel_싸피"
class FragmentShopViewModel() : ViewModel() {

    private val userRepository = UserRepository.get()
    private val shopRepository = ShopRepository.get()

    val shopList: LiveData<MutableList<Shop>> = shopRepository.getShop()
    var user = User("temp", "tempPW",0)

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

    fun updateUser(price: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                user.Point -= price
                userRepository.updateUser(user)
            }
        }
    }
}