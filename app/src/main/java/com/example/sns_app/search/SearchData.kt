package com.example.sns_app.search

import com.google.firebase.storage.StorageReference

//총 user에 대한 data class
data class SearchData(
    val uid: String, // 검색한 유저 activity에 uid 전달
    val id: String,
    val name: String,
    val img: StorageReference // 참조를 얻어옴
)
