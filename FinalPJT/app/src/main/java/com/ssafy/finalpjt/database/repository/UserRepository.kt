package com.ssafy.finalpjt.database.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.room.withTransaction
import com.ssafy.finalpjt.database.CarrotDatabase
import com.ssafy.finalpjt.database.dao.GoalDao
import com.ssafy.finalpjt.database.dto.Goal
import com.ssafy.finalpjt.database.dto.Shop
import com.ssafy.finalpjt.database.dto.User

class UserRepository (private val db: CarrotDatabase){


    private val userDao=db.userDao()

    fun getUser(id:Int): User {
        return userDao.getUser(id)
    }
    suspend fun insertUser(user: User)=db.withTransaction{
        userDao.insertUser(user)
    }


    companion object{
        private var INSTANCE : UserRepository? =null

        fun initialize(db: CarrotDatabase){
            if(INSTANCE == null){
                INSTANCE = UserRepository(db)
            }
        }

        fun get() : UserRepository {
            return INSTANCE ?:
            throw IllegalStateException("NoteRepository must be initialized")
        }
    }
}