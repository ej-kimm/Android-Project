package com.example.sns_app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sns_app.Posting.PostingData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeViewModel : ViewModel() {
    private val _posts = MutableLiveData<List<PostingData>>()
    val posts: LiveData<List<PostingData>> get() = _posts
    val list: MutableList<PostingData> = mutableListOf()

    private val db = Firebase.firestore
    private val itemsCollectionRef = db.collection("posting")

    private fun setData(data: List<PostingData>) {
        _posts.value = data
    }

    fun createList(uid: String) { // 획득한 uid의 게시글 정보 불러오기
        itemsCollectionRef.get().addOnSuccessListener {
            for (document in it) {
                val data = PostingData(
                    context = document["context"].toString(),
                    imageURL = document["imageURL"].toString(),
                    time = document["time"].toString(),
                    userID = document["userID"].toString(),
                    UID = document["uid"].toString(),
                    profileURL = document["profileURL"].toString(),
                    heartClickPeople = hashMapOf(),
                    heartCount = 0
                )
                if (document["uid"].toString() == uid && !list.contains(data)) {
                    list.add(
                        data
                    )
                    setData(list)
                }
            }
        }
    }
}