package com.ssafy.finalpjt.database.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.room.withTransaction
import com.ssafy.finalpjt.database.CarrotDatabase
import com.ssafy.finalpjt.database.dao.GoalDao
import com.ssafy.finalpjt.database.dto.Goal
import com.ssafy.finalpjt.database.dto.GoalSub

class GoalSubRepository (private val db: CarrotDatabase){


    private val goalsubDao=db.goalSubDao()

    fun getGoalSub(goalId:Long):LiveData<MutableList<GoalSub>>{
        return goalsubDao.getGoalSub(goalId)
    }
    suspend fun insertGoalSub(goalSub: GoalSub)=db.withTransaction{
        goalsubDao.insertGoalSub(goalSub)
    }
    suspend fun updateGoalSub(goalSub: GoalSub)=db.withTransaction{
        goalsubDao.updateGoalSub(goalSub.Completed,goalSub.id)
    }
    suspend fun deleteGoalSub(goalSub: GoalSub)=db.withTransaction{
        goalsubDao.deleteGoalSub(goalSub)
    }

    suspend fun deleteGoalSubById(id: Long)=db.withTransaction {
        goalsubDao.deleteGoalSubById(id)
    }

    companion object{
        private var INSTANCE : GoalSubRepository? =null

        fun initialize(db: CarrotDatabase){
            if(INSTANCE == null){
                INSTANCE = GoalSubRepository(db)
            }
        }

        fun get() : GoalSubRepository {
            return INSTANCE ?:
            throw IllegalStateException("NoteRepository must be initialized")
        }
    }
}