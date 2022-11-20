package com.ssafy.finalpjt.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ssafy.finalpjt.database.dto.GoalSub
import com.ssafy.finalpjt.database.dto.Todo
@Dao

interface TodoDao {
    @Query("SELECT * FROM Todo WHERE Date=(:date)")
    fun getTodayTodo(date:Int):LiveData<MutableList<Todo>>

    @Insert
    suspend fun insertTodo(todo: Todo)

}