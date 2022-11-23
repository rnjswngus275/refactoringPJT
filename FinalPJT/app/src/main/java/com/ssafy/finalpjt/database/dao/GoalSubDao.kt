package com.ssafy.finalpjt.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ssafy.finalpjt.database.dto.GoalSub

@Dao
interface GoalSubDao {
    @Query("SELECT * FROM GoalSub WHERE GoalId=(:goalId)")
    fun getGoalSub(goalId:Long): LiveData<MutableList<GoalSub>>

    @Insert
    suspend fun insertGoalSub(goalSub: GoalSub)

    @Query("UPDATE GoalSub SET Completed=(:Complted) WHERE id=(:id)")
    suspend fun updateGoalSub(Complted:Int,id:Long)

    @Delete
    suspend fun deleteGoalSub(goalSub: GoalSub)

    @Query("DELETE FROM GoalSub WHERE id=(:id)")
    suspend fun deleteGoalSubById(id: Long)

}