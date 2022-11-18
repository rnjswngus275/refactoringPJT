package com.ssafy.finalpjt.db.database

import android.app.Application
import com.ssafy.finalpjt.db.database.repository.GoalRepository

class DatabaseApplicationClass: Application() {
    override fun onCreate() {
        super.onCreate()
        GoalRepository.initialize(this)
    }
}