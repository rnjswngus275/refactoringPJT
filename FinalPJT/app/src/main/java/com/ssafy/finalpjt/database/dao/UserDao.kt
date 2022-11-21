package com.ssafy.finalpjt.database.dao

import androidx.room.*
import com.ssafy.finalpjt.database.dto.GoalSub
import com.ssafy.finalpjt.database.dto.User

@Dao

interface UserDao {
    @Query("SELECT * FROM User ")
    fun getUser(): User

    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM User WHERE UserName=(:name)")
    suspend fun getUserById(name:String): User

    @Update
    suspend fun updatePoint(user:User)

}