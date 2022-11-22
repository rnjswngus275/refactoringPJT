package com.ssafy.finalpjt.database.repository

import androidx.lifecycle.LiveData
import androidx.room.withTransaction
import com.ssafy.finalpjt.database.CarrotDatabase
import com.ssafy.finalpjt.database.dto.User

class UserRepository (private val db: CarrotDatabase){

    private val userDao=db.userDao()

    fun getUser(id:Long): LiveData<User> {
        return userDao.getUser(id)
    }

    fun getUser(): LiveData<MutableList<User>> {
        return userDao.getUser()
    }

    suspend fun insertUser(user: User)=db.withTransaction{
        userDao.insertUser(user)
    }

    suspend fun getUserByName(name:String)=db.withTransaction{
        userDao.getUserByName(name)
    }

    suspend fun updateUser(user: User)=db.withTransaction {
        userDao.updateUser(user)
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