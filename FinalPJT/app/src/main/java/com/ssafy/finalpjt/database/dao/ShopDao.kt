package com.ssafy.finalpjt.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.ssafy.finalpjt.database.dto.Shop
@Dao
interface ShopDao {
    @Query("SELECT * FROM Shop")
    fun getShop(): LiveData<ArrayList<Shop>>

    @Insert
    suspend fun insertShop(shop: Shop)

    @Delete
    suspend fun deleteShop(shop: Shop)
}