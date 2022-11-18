package com.ssafy.finalpjt.database.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.room.withTransaction
import com.ssafy.finalpjt.database.CarrotDatabase
import com.ssafy.finalpjt.database.dao.GoalDao
import com.ssafy.finalpjt.database.dto.Goal
import com.ssafy.finalpjt.database.dto.Shop

class ShopRepository (private val db: CarrotDatabase){


    private val shopDao=db.shopDao()

    fun getShop():LiveData<ArrayList<Shop>>{
        return shopDao.getShop()
    }
    suspend fun insertShop(shop: Shop)=db.withTransaction{
        shopDao.insertShop(shop)
    }
    suspend fun deleteShop(shop: Shop)=db.withTransaction{
        shopDao.deleteShop(shop)
    }

    companion object{
        private var INSTANCE : ShopRepository? =null

        fun initialize(db: CarrotDatabase){
            if(INSTANCE == null){
                INSTANCE = ShopRepository(db)
            }
        }

        fun get() : ShopRepository {
            return INSTANCE ?:
            throw IllegalStateException("NoteRepository must be initialized")
        }
    }
}