package com.ssafy.finalpjt

import android.content.Context
import android.content.SharedPreferences
import com.ssafy.finalpjt.database.dto.User

class SharedPreferencesUtil(context: Context) {
    val SHARED_PREFERENCES_NAME = "carrot_pref"

    val pref : SharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    // 현재 로그인 된 사용자 정보
    fun addUser(userName: String, userPw: String) {
        val editor = pref.edit()
        editor.putString("UserName", userName)
        editor.putString("UserPw", userPw)
        editor.apply()
    }

    fun getUserName(): String {
        val name = pref.getString("UserName", "")
        return name ?: ""
    }

}