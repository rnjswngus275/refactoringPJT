package com.ssafy.finalpjt.db.database.dao

import androidx.room.*
import com.ssafy.finalpjt.db.database.dto.GoalSub
import com.ssafy.finalpjt.db.database.dto.Todo
@Dao

interface TodoDao {
    @Query("SELECT * FROM Todo WHERE Date=(:date)")
    suspend fun getTodayTodo(date:Int):ArrayList<Todo>

    @Insert
    suspend fun insertTodo(todo: Todo)

}