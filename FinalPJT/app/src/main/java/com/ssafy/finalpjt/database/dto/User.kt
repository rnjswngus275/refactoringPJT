package com.ssafy.finalpjt.database.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="User")

data class User(
    var UserName:String,
    var Point:Int
){
    @PrimaryKey(autoGenerate = true)
    var id :Int=0

    constructor(_id:Int,UserName: String,Point: Int):this(UserName,Point){
        this.id=id
    }
}
