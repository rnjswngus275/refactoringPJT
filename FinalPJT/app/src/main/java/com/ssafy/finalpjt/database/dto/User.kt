package com.ssafy.finalpjt.database.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="User")

data class User(
    var UserName:String,
    var UserPw:String,
    var Point:Int
){
    @PrimaryKey(autoGenerate = true)
    var id :Long=0

    constructor(_id:Int,UserName: String,UserPw: String,Point: Int):this(UserName, UserPw, Point){
        this.id=id
    }
}
