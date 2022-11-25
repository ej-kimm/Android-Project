package com.example.sns_app.Search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class UserPageViewModel() : ViewModel() {
    private val _posts = MutableLiveData<List<SearchUserData>>()
    val posts: LiveData<List<SearchUserData>> get() = _posts

    private val db = Firebase.firestore
    private val itemsCollectionRef = db.collection("posting").orderBy("time", Query.Direction.DESCENDING)

    init {
//        createList()
    }

    private fun setData(data: List<SearchUserData>) {
        _posts.value = data
    }

    fun createList(uid: String) {
        itemsCollectionRef.get().addOnSuccessListener {
            val list: MutableList<SearchUserData> = mutableListOf()
            for (document in it!!) {
                if (document["uid"].toString() == uid){ // 인자로 전달받은 uid 정보 띄우기
                    list.add(
                        SearchUserData(
                            context = document["context"].toString(),
                            imageURL = document["imageURL"].toString(),
                            time = document["time"].toString(),
                            userID = document["userID"].toString(),
                            UID = document["uid"].toString(),
                            profileURL = document["profileURL"].toString(),
                            heartClickPeople = hashMapOf(),
                            heartCount = 0
                        )
                    )
                    setData(list)
                }
            }
        }
    }
}
