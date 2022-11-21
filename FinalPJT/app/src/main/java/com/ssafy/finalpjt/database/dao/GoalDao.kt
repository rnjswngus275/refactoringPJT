package com.ssafy.finalpjt.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ssafy.finalpjt.database.dto.Goal

@Dao
interface GoalDao {
    @Query("SELECT * FROM Goal")
    fun getGoal(): LiveData<ArrayList<Goal>>

    @Insert
    suspend fun insertGoal(goal: Goal)

    @Update
    suspend fun updateGoal(goal: Goal)

    @Delete
    suspend fun deleteGoal(goal: Goal)

    @Query("SELECT GoalTitle FROM Goal")
    fun getGoalTtitle():LiveData<ArrayList<String>>

    @Query("SELECT id FROM Goal WHERE GoalTitle=(:goaltitle)")
    fun getGoalId(goaltitle:String):Int
}