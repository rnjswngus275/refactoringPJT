package com.ssafy.finalpjt.db.database.dto

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(tableName="GoalSub",
    foreignKeys=[
        ForeignKey(
            entity = Goal::class,
            parentColumns= ["id"],
            childColumns = ["GoalId"],
            onDelete = CASCADE
        )
    ]
)

data class GoalSub(
    var GoalId: Int = -1,
    var SubTitle: String? = null,
    var Completed: Int = 0
){
    @PrimaryKey(autoGenerate = true)
    var id :Int=0

    constructor(id:Int,GoalId: Int,SubTitle:String,Completed: Int):this(GoalId,SubTitle,Completed){
        this.id=id
    }

}