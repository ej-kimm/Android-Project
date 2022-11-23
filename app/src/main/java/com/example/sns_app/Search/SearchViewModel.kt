package com.example.sns_app.Search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class SearchViewModel : ViewModel() {

    var db : FirebaseFirestore = Firebase.firestore
    var itemsCollectionRef = db.collection("usersInformation")
    private val storage = Firebase.storage

    private val currentUid = Firebase.auth.currentUser!!.uid
    //val doc = itemsCollectionRef.document(currentUid)

    private val _searchData = MutableLiveData<List<SearchData>>()
    val searchData: LiveData<List<SearchData>> get() = _searchData

    private val _retrieved = MutableLiveData<List<SearchData>>()
    val retrieved: LiveData<List<SearchData>> get() = _retrieved

    //시작하면 데이터 받아오기
    init {
        createList()
    }
    /*
    fun setRetriedList(list: List<SearchData>) {
        _retrieved.value = list
    }
     */
    fun emptyInput() {
        _retrieved.value = searchData.value
    }

    fun searchingList(p0: String?) {
        val list: MutableList<SearchData> = mutableListOf()
        searchData.value?.forEachIndexed { i, v ->

            if(v.id.lowercase().contains(p0!!.lowercase()) || v.name.lowercase().contains(p0.lowercase())){
                    list.add(
                        SearchData(
                            v.uid,
                            v.id,
                            v.name,
                            v.img
                        )
                    )

            }
        }
        _retrieved.value = list
    }

    fun setData(data: List<SearchData>) {
        _searchData.value = data
    }

    // 임시적으로 데이터 추가해서 list 반환
    private fun createList() {
        itemsCollectionRef.get().addOnSuccessListener {
            val list: MutableList<SearchData> = mutableListOf()
            for (document in it!!) {
                val profileImgRef = storage.getReference("ProfileImage/${document["profileImage"].toString()}")
                if(document.id != currentUid) {
                    list.add(
                        SearchData(
                            uid = document.id,
                            id = document["userID"].toString(),
                            name = document["name"].toString(),
                            img = profileImgRef // 참조를 전달
                        )
                    )
                }
            }
            _searchData.value = list
        }
    }
}