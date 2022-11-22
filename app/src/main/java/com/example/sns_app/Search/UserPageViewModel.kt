package com.example.sns_app.Search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class UserPageViewModel() : ViewModel() {
    private val _posts = MutableLiveData<List<SearchUserData>>()
    val posts: LiveData<List<SearchUserData>> get() = _posts

    private val db = Firebase.firestore
    private val itemsCollectionRef = db.collection("posting")

    init {
//        createList()
    }

    private fun setData(data: List<SearchUserData>) {
        _posts.value = data
    }

    fun createList(uid: String) {
        val list: MutableList<SearchUserData> = mutableListOf()

        itemsCollectionRef.addSnapshotListener { snapshot, _ ->
            for (document in snapshot!!) {
                if (document["uid"].toString() == uid){ // 인자로 전달받은 uid 정보 띄우기
                    list.add(0,
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
