package com.example.sns_app.myPage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sns_app.Posting.PostingData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MyPageViewModel : ViewModel() {
    private val _posts = MutableLiveData<List<PostingData>>()
    val posts: LiveData<List<PostingData>> get() = _posts
    private val db = Firebase.firestore
    private val itemsCollectionRef = db.collection("posting")

    init {
        createList()
    }

    private fun setData(data: List<PostingData>) {
        _posts.value = data
    }

    private fun createList() {
        val list: MutableList<PostingData> = mutableListOf()

        itemsCollectionRef.addSnapshotListener { snapshot, _ ->
            for (document in snapshot!!) {
                if (document["uid"].toString() == Firebase.auth.currentUser!!.uid){
                    list.add(0,
                        PostingData(
                            context = document["context"].toString(),
                            imageURL = document["imageURL"].toString(),
                            time = document["time"].toString(),
                            userID = document["userID"].toString(),
                            UID = document["uid"].toString(),
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