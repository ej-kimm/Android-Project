package com.example.sns_app.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.sns_app.MainActivity
import com.example.sns_app.databinding.ActivityLoginBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 로그인 버튼을 눌렀을 때
        binding.btnLogin.setOnClickListener {
            val userEmail = binding.email.text.toString()
            val userPassword = binding.password.text.toString()
            if (canLogin(userEmail, userPassword))
                doLogin(userEmail, userPassword)
        }

        // 회원가입 버튼을 눌렀을 때
        binding.btnSignUp.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

    }

    private fun canLogin(userEmail: String, userPassword: String): Boolean {
        if (userEmail.isEmpty()) {
            Toast.makeText(applicationContext, "이메일을 입력해주세요", Toast.LENGTH_LONG).show()
            return false
        }
        else if (userPassword.isEmpty()) {
            Toast.makeText(applicationContext, "비밀번호를 입력해주세요", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun doLogin(userEmail: String, password: String) {
        Firebase.auth.signInWithEmailAndPassword(userEmail, password)
            .addOnCompleteListener(this) {
                if(it.isSuccessful) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Log.w("LoginActivity", "signInWithEmail", it.exception)
                    Toast.makeText(this, "로그인을 실패했습니다", Toast.LENGTH_SHORT).show()
                }
            }
    }
}