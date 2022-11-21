package com.ssafy.finalpjt.database.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.room.withTransaction
import com.ssafy.finalpjt.database.CarrotDatabase
import com.ssafy.finalpjt.database.dao.GoalDao
import com.ssafy.finalpjt.database.dto.Goal

class GoalRepository (private val db: CarrotDatabase){


    private val goalDao=db.goalDao()

    fun getGoal():LiveData<ArrayList<Goal>>{
        return goalDao.getGoal()
    }
    suspend fun insertGoal(goal: Goal)=db.withTransaction{
        goalDao.insertGoal(goal)
    }
    suspend fun updateGoal(goal: Goal)=db.withTransaction{
        goalDao.updateGoal(goal)
    }
    suspend fun deleteGoal(goal: Goal)=db.withTransaction{
        goalDao.deleteGoal(goal)
    }
    fun getGoalTitle(): LiveData<ArrayList<String>> {
        return goalDao.getGoalTtitle()
    }
    fun getGoalId(title:String): Int {
        return goalDao.getGoalId(title)
    }

    companion object{
        private var INSTANCE : GoalRepository? =null

        fun initialize(db: CarrotDatabase){
            if(INSTANCE == null){
                INSTANCE = GoalRepository(db)
            }
        }

        fun get() : GoalRepository {
            return INSTANCE ?:
            throw IllegalStateException("NoteRepository must be initialized")
        }
    }
}