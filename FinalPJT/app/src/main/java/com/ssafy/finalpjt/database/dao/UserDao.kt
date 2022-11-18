package com.ssafy.finalpjt.database.dao

import androidx.room.*
import com.ssafy.finalpjt.database.dto.GoalSub
import com.ssafy.finalpjt.database.dto.User

@Dao

interface UserDao {
    @Query("SELECT * FROM User WHERE id=(:id)")
    fun getUser(id:Int): User

    @Insert
    suspend fun insertUser(user: User)

}