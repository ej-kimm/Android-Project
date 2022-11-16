package com.example.sns_app.Login

data class UserInformation(val nickname: String, val password: String, val birth: String,
                           val email: String, val userID: String) {
    fun userFindHashMap(): HashMap<String, String> {
        return hashMapOf(
            "name" to nickname,
            "password" to password,
            "birth" to birth,
            "email" to email,
            "userID" to userID
        )
    }
}