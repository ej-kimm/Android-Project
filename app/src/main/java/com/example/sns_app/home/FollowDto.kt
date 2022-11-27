package com.example.sns_app.home

data class FollowDto ( // 팔로우 관계를 주고 받기 위한 데이터 클래스
    var followers : MutableMap<String, Boolean> = HashMap(),
    var followings : MutableMap<String, Boolean> = HashMap()
)