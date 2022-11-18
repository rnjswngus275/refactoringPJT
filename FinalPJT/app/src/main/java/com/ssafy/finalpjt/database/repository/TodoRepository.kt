package com.ssafy.finalpjt.database.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.room.withTransaction
import com.ssafy.finalpjt.database.CarrotDatabase
import com.ssafy.finalpjt.database.dao.GoalDao
import com.ssafy.finalpjt.database.dto.Goal
import com.ssafy.finalpjt.database.dto.Todo

class TodoRepository (private val db: CarrotDatabase){


    private val todoDao=db.todoDao()

    fun getTodayTodo(date:Int):LiveData<ArrayList<Todo>>{
        return todoDao.getTodayTodo(date)
    }
    suspend fun insertTodo(todo: Todo)=db.withTransaction{
        todoDao.insertTodo(todo)
    }


    companion object{
        private var INSTANCE : TodoRepository? =null

        fun initialize(db: CarrotDatabase){
            if(INSTANCE == null){
                INSTANCE = TodoRepository(db)
            }
        }

        fun get() : TodoRepository {
            return INSTANCE ?:
            throw IllegalStateException("NoteRepository must be initialized")
        }
    }
}