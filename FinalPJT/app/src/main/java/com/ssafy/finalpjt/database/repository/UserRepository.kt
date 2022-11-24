package com.ssafy.finalpjt.database.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.withTransaction
import com.ssafy.finalpjt.database.CarrotDatabase
import com.ssafy.finalpjt.database.dto.User

private const val TAG = "UserRepository"
class UserRepository (private val db: CarrotDatabase){

    private val userDao=db.userDao()

    fun getUser(id:Long): LiveData<User> {
        return userDao.getUser(id)
    }

    fun getUserByName(name:String) : LiveData<User> {
        return userDao.getUserByName(name)
    }

    fun getAllUser(): LiveData<MutableList<User>> {
        return userDao.getAllUser()
    }

    suspend fun insertUser(user: User)=db.withTransaction{
        userDao.insertUser(user)
    }

    suspend fun updateUser(user: User)=db.withTransaction {
        userDao.updateUser(user)
    }

    suspend fun updateUserPoint(point: Int, userName: String)=db.withTransaction {
        userDao.updateUserPoint(point, userName)
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