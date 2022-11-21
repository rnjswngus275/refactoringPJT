package com.ssafy.finalpjt.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ssafy.finalpjt.database.dto.GoalSub
import com.ssafy.finalpjt.database.dto.User

@Dao

interface UserDao {
    @Query("SELECT * FROM User WHERE id=(:id)")
    fun getUser(id:Long): User

    @Query("SELECT * FROM User ")
    fun getUser(): User

    @Insert
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Query("SELECT * FROM User WHERE UserName=(:name)")
    suspend fun getUserById(name:String): User


}