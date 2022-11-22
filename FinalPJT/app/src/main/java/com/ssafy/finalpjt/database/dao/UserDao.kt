package com.ssafy.finalpjt.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ssafy.finalpjt.database.dto.GoalSub
import com.ssafy.finalpjt.database.dto.User

@Dao

interface UserDao {
    @Query("SELECT * FROM User WHERE id=(:id)")
    fun getUser(id:Long): LiveData<User>

    @Query("SELECT * FROM User ")
    fun getUser(): LiveData<MutableList<User>>

    @Insert
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Query("SELECT * FROM User WHERE UserName=(:name)")
    fun getUserByName(name:String): LiveData<User>


}