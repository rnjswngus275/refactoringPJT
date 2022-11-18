package com.ssafy.finalpjt.database
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ssafy.finalpjt.database.dao.*
import com.ssafy.finalpjt.database.dto.*
import com.ssafy.finalpjt.db.database.dao.*
import com.ssafy.finalpjt.db.database.dto.*
import kotlinx.coroutines.CoroutineScope

private const val DATABASE_NAME = "Carrot"

@Database(entities=[Goal::class, GoalSub::class, Shop::class, Todo::class, User::class],version=1)
abstract class CarrotDatabase :RoomDatabase(){
    abstract fun goalDao(): GoalDao
    abstract fun goalSubDao(): GoalSubDao
    abstract fun shopDao(): ShopDao
    abstract fun todoDao(): TodoDao
    abstract fun userDao(): UserDao

    companion object{
        private var Instance: CarrotDatabase?=null
        fun getInstance(context: Context): CarrotDatabase {
            return Instance ?: synchronized(this){
                val instance= Room.databaseBuilder(
                    context.applicationContext,
                    CarrotDatabase::class.java,
                    DATABASE_NAME
                ).build()
                Instance =instance
                instance
            }
        }
    }

}