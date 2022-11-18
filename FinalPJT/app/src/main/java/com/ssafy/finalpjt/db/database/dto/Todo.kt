package com.ssafy.finalpjt.db.database.dto

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName="Todo",
    foreignKeys=[
        ForeignKey(
            entity = Goal::class,
            parentColumns= ["id"],
            childColumns = ["GoalId"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class Todo(    var Todo: String = "",
                    var Date: Int = 0,
                    var GoalId:Int=0,
                    var Completed: Int = 0) {

    @PrimaryKey(autoGenerate = true)
    var id :Int=0

    constructor(id:Int,Todo: String,Date: Int,GoalId: Int,Completed: Int):this(Todo,Date,GoalId,Completed){
        this.id=id
    }
}