package com.ssafy.finalpjt.database

import android.app.Application
import android.content.SharedPreferences
import com.ssafy.finalpjt.SharedPreferencesUtil
import com.ssafy.finalpjt.database.repository.*

class DatabaseApplicationClass: Application() {
    companion object {
        lateinit var sharedPreferencesUtil: SharedPreferencesUtil
    }

    override fun onCreate() {
        super.onCreate()
        sharedPreferencesUtil = SharedPreferencesUtil(applicationContext)
        val db by lazy{ CarrotDatabase.getInstance(this) }
        GoalRepository.initialize(db)
        GoalSubRepository.initialize(db)
        ShopRepository.initialize(db)
        TodoRepository.initialize(db)
        UserRepository.initialize(db)
    }
}