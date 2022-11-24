package com.ssafy.finalpjt.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.ssafy.finalpjt.viewmodel.LoginViewModel
import com.ssafy.finalpjt.database.dto.User
import com.ssafy.finalpjt.databinding.ActivityJoinBinding

class JoinActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJoinBinding
    private val loginViewModel : LoginViewModel by viewModels()
    var userList = mutableListOf<User>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        loginViewModel.userList.observe(this) {
            this@JoinActivity.userList = it
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }

        binding.btnJoin.setOnClickListener {
            var joinUser = User(
                binding.etJoinId.text.toString(),
                binding.etJoinPw.text.toString(),
                0
            )
            if (checkId(joinUser)) {
                loginViewModel.insertUser(joinUser)
                Toast.makeText(this, "가입되었습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun checkId(joinUser: User) : Boolean {
        for (user in userList) {
            if (joinUser.UserName == user.UserName) {
                Toast.makeText(this, "중복된 ID가 있습니다.", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
    }
}