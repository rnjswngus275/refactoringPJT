package com.ssafy.finalpjt.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ssafy.finalpjt.database.dto.GoalSub

@Dao
interface GoalSubDao {
    @Query("SELECT * FROM GoalSub WHERE GoalId=(:goalId)")
    fun getGoalSub(goalId:Int): LiveData<ArrayList<GoalSub>>

    @Insert
    suspend fun insertGoalSub(goalSub: GoalSub)

    @Update
    suspend fun updateGoalSub(goalSub: GoalSub)

    @Delete
    suspend fun deleteGoalSub(goalSub: GoalSub)


}