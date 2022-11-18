package com.ssafy.finalpjt.db.database.dao

import androidx.room.*
import com.ssafy.finalpjt.db.database.dto.GoalSub

@Dao
interface GoalSubDao {
    @Query("SELECT * FROM GoalSub WHERE GoalId=(:goalId)")
    suspend fun getGoalSub(goalId:Int):LiveData<ArrayList<GoalSub>>

    @Insert
    suspend fun insertGoalSub(goalSub: GoalSub)

    @Update
    suspend fun updateGoalSub(goalSub: GoalSub)

    @Delete
    suspend fun deleteGoalSub(goalSub: GoalSub)

}