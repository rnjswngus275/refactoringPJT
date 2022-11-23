package com.ssafy.finalpjt.activity

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.ssafy.finalpjt.LoginViewModel
import com.ssafy.finalpjt.R
import com.ssafy.finalpjt.database.DatabaseApplicationClass
import com.ssafy.finalpjt.database.dto.User
import com.ssafy.finalpjt.databinding.ActivityLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.log

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private val loginViewModel : LoginViewModel by viewModels()
    var userList = mutableListOf<User>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel.userList.observe(this) {
            this@LoginActivity.userList = it
        }

        binding.btnLogin.setOnClickListener {
            var loginUser = User(
                binding.etLoginId.text.toString(),
                binding.etLoginPw.text.toString(),
                0
            )
            for (user in userList) {
                if (loginUser.UserName == user.UserName && loginUser.UserPw == user.UserPw) {
                    DatabaseApplicationClass.sharedPreferencesUtil.addUser(loginUser.UserName, loginUser.UserPw)
                    val intent = Intent(this, MainActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    }
                    startActivity(intent)
                }
            }
        }

        binding.btnJoin.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
        }
    }
}