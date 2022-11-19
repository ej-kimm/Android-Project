package com.example.sns_app.Search

import com.google.firebase.storage.StorageReference

//총 user에 대한 data class
data class SearchData(
    val id: String,
    val name: String,
    val img: StorageReference // 참조를 얻어옴
)
