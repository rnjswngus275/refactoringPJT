package com.ssafy.finalpjt.db.database.dao

import androidx.room.*
import com.ssafy.finalpjt.db.database.dto.GoalSub
import com.ssafy.finalpjt.db.database.dto.User

@Dao

interface UserDao {
    @Query("SELECT * FROM User WHERE id=(:id)")
    suspend fun getUser(id:Int): User

    @Insert
    suspend fun insertGoalSub(user: User)

}