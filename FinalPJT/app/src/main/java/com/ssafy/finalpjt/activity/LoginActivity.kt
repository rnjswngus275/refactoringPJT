package com.ssafy.finalpjt.activity

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.ssafy.finalpjt.R
import com.ssafy.finalpjt.database.DatabaseApplicationClass
import com.ssafy.finalpjt.database.dto.User
import com.ssafy.finalpjt.databinding.ActivityLoginBinding
import com.ssafy.finalpjt.viewmodel.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.log

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private val loginViewModel : LoginViewModel by viewModels()
    private var userList = mutableListOf<User>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        loginViewModel.userList.observe(this) {
            this@LoginActivity.userList = it
        }

        binding.btnLogin.setOnClickListener {
            val loginUser = User(
                binding.etLoginId.text.toString(),
                binding.etLoginPw.text.toString(),
                0
            )
            if (checkId(loginUser)) {
                val intent = Intent(this, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "잘못된 로그인 정보입니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnJoin.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkId(loginUser: User) : Boolean {
        for (user in userList) {
            if (loginUser.UserName == user.UserName && loginUser.UserPw == user.UserPw) {
                DatabaseApplicationClass.sharedPreferencesUtil.addUser(loginUser.UserName, loginUser.UserPw)
                return true
            }
        }
        return false
    }
}