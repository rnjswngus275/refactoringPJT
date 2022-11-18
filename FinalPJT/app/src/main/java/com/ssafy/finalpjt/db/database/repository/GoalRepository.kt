package com.ssafy.finalpjt.db.database.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.ssafy.finalpjt.db.database.CarrotDatabase
import com.ssafy.finalpjt.db.database.dto.Goal

val DATABASE_NAME="Carrot"
class GoalRepository private constructor(context: Context){

    private val database : CarrotDatabase = Room.databaseBuilder(
        context.applicationContext,
        CarrotDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val goalDao=database.goalDao()

    fun getGoal():LiveData<ArrayList<Goal>>{

    }

}