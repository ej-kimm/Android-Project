package com.example.sns_app.Follow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FollowViewModel : ViewModel() {

    private val _follower = MutableLiveData<List<FollowData>>()
    val follower: LiveData<List<FollowData>> get() = _follower

    fun setFollowList(list: List<FollowData>) {
        _follower.value = list
    }
}