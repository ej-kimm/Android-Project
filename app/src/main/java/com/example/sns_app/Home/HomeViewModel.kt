package com.example.sns_app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sns_app.Home.FollowDto
import com.example.sns_app.Posting.PostingData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeViewModel : ViewModel() {
    private val _posts = MutableLiveData<List<PostingData>>()
    val posts: LiveData<List<PostingData>> get() = _posts

    private val db = Firebase.firestore
    private val itemsCollectionRef = db.collection("posting")

    fun getFollowInfo(followDto: FollowDto) {
        createList(followDto.followings.keys)
    }

    fun createList(uidList: MutableSet<String>) { // 획득한 uid의 게시글 정보 불러오기
        itemsCollectionRef.get().addOnSuccessListener {
            val list: MutableList<PostingData> = mutableListOf()
            for (doc in it) {
                for (uid in uidList) {
                    if (doc["uid"].toString() == uid)
                        list.add(
                            PostingData(
                                context = doc["context"].toString(),
                                imageURL = doc["imageURL"].toString(),
                                time = doc["time"].toString(),
                                userID = doc["userID"].toString(),
                                UID = doc["uid"].toString(),
                                profileURL = doc["profileURL"].toString(),
                                heartClickPeople = hashMapOf(),
                                heartCount = 0
                            )
                        )
                    }
                }
            _posts.value = list
        }
    }
}
