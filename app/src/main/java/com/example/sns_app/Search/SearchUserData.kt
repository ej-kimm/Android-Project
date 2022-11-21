package com.example.sns_app.Search

import com.google.firebase.storage.StorageReference

data class SearchUserData (
    val searchUserId : String,
    val searchUserId2 : String,
    val searchUserComment : String,
    //나의 프로필 사진
    val my_img : String,
    //검색 유저 프로필 사진
    val searchUser_img : String,
    //검색 유저 게시글
    val searchUserPost_img : String

)