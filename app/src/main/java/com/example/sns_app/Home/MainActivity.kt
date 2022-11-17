package com.example.sns_app.Home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.sns_app.Login.LoginActivity
import com.example.sns_app.R
import com.example.sns_app.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 검색 기능에서 키보드 자판이 화면 끌고 올라가는 것을 방지하기 위함(최상위 Activity에 먼저 적용해야함)
//        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        // 레이아웃 수정으로 해결, 방식 논의 후 결정 예정

        if(Firebase.auth.currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // 바텀 네비게이션 구현
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNavigationView?.setupWithNavController(navController)

        /* 로그아웃 버튼 임시로 만들어둔것. auth 테스트용 */
//        binding.button.setOnClickListener {
//            Firebase.auth.signOut()
//            finish()
//        }
    }
}