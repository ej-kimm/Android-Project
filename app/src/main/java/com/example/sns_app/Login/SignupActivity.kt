package com.example.sns_app.Login

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.sns_app.databinding.ActivitySignupBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class SignupActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignupBinding
    lateinit var userInformation: UserInformation // 유저 정보들을 담을 data class
    private val db = Firebase.firestore
    private val usersCollectionRef = db.collection("usersInformation")
    private var calendar = Calendar.getInstance()
    private var year = calendar.get(Calendar.YEAR)
    private var month = calendar.get(Calendar.MONTH)
    private var day = calendar.get(Calendar.DAY_OF_MONTH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSelectBirth.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this, { _, year, month, day ->
                binding.signupBirth.text = year.toString() + "-" + (month + 1).toString() + "-" + day.toString()
            }, year, month, day)
            datePickerDialog.show()
        }

        binding.btnSignupBtn.setOnClickListener {
            val signup_nickname = binding.signupNickname.text.toString()
            val signup_password = binding.signupPassword.text.toString()
            val signup_birth = binding.signupBirth.text.toString()
            val signup_email = binding.signupEmail.text.toString()
            val userID = signup_email.split("@")[0] // 유저가 입력한 (아이디@gmail.com) @앞까지 자름

            userInformation = UserInformation(signup_nickname, signup_password, signup_birth, signup_email, userID) // UserInformation 데이터 클래스에 담음
            if(signup_nickname.isNotEmpty() && signup_password.isNotEmpty() && signup_birth.isNotEmpty() && signup_email.isNotEmpty()) { // 공백이 아닐 경우에만 가입
                if (canSignup(userInformation)) { // 제약조건 확인
                    doSignUp(userInformation)
                }
            }
        }
    }

    // collection에 user의 정보를 담음(식별자는 UID)
    private fun addInformation(users: UserInformation) {
        val itemMap = users.userHashMap();
        usersCollectionRef.add(itemMap) // 고유 UID 밑에 아이디, 이름, 생일, 비밀번호, 이메일저장
    }

    private fun doSignUp(users: UserInformation) {
        Firebase.auth.createUserWithEmailAndPassword(users.email, users.password)
            .addOnCompleteListener(this) {
                if(it.isSuccessful) {
                    addInformation(users) // collection에 user의 정보를 담음
                    Toast.makeText(this, "환영합니다. 로그인을 시도해주세요", Toast.LENGTH_SHORT).show()
                    finish() // 회원가입 완료하면 로그인창으로 돌아가서 로그인하도록 함
                } else {
                    Log.w("LoginActivity", "signInWithEmail", it.exception)
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun canSignup(users: UserInformation): Boolean {
        return if (users.nickname.length < 2) {
            Toast.makeText(this, "닉네임을 2자이상 입력해주세요", Toast.LENGTH_SHORT).show()
            false
        } else if (users.password.length < 6) {
            Toast.makeText(this, "비밀번호 6자이상 입력해주세요", Toast.LENGTH_SHORT).show()
            false
        } else true
    }

}