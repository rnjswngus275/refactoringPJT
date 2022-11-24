package com.ssafy.finalpjt.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ssafy.finalpjt.database.dto.GoalSub
import com.ssafy.finalpjt.database.dto.User

@Dao

interface UserDao {


    @Query("SELECT * FROM User")
    fun getAllUser(): LiveData<MutableList<User>>

    @Insert
    suspend fun insertUser(user: User)

    @Query("UPDATE User SET Point=(:point) WHERE UserName=(:userName)")
    suspend fun updateUserPoint(point: Int, userName: String)

    @Query("SELECT * FROM User WHERE UserName=(:name)")
    fun getUserByName(name: String): LiveData<User>


}