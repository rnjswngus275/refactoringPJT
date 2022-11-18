package com.ssafy.finalpjt.db.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.ssafy.finalpjt.db.database.dto.Shop
@Dao
interface ShopDao {
    @Query("SELECT * FROM Shop")
    suspend fun getShop():ArrayList<Shop>

    @Insert
    suspend fun insertShop(shop: Shop)

    @Delete
    suspend fun deleteShop(shop: Shop)
}