package com.example.sns_app.Posting

// 글 내용
data class PostingData(
    var context: String,
    var imageURL: String, // 사진 url
    var UID: String, // 고유 UID
    var userID: String, // 글을 작성한 유저의 아이디
    var profileURL: String,
    var time: String, // 글을 올린 시간
    var heartCount: Int = 0, // 해당 글의 하트 수(좋아요)
    var heartClickPeople: Map<String, Boolean> = HashMap()) { // 하트 누른 사람 관리

    // 해당 글의 댓글 관리 (아직 사용안함)
    data class Comment(var UID: String,
                       var userID: String,
                       var comment: String,
                       var time: String)
}