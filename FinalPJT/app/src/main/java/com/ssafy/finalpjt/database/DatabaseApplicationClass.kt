package com.ssafy.finalpjt.database

import android.app.Application
import com.ssafy.finalpjt.database.repository.*
import com.ssafy.finalpjt.db.database.repository.*

class DatabaseApplicationClass: Application() {
    override fun onCreate() {
        super.onCreate()
        val db by lazy{ CarrotDatabase.getInstance(this) }
        GoalRepository.initialize(db)
        GoalSubRepository.initialize(db)
        ShopRepository.initialize(db)
        TodoRepository.initialize(db)
        UserRepository.initialize(db)
    }
}