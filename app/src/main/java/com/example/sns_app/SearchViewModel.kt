package com.example.sns_app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sns_app.R

class SearchViewModel : ViewModel() {
    private val _searchData = MutableLiveData<List<SearchData>>()
    val searchData: LiveData<List<SearchData>> get() = _searchData


    private val _retrieved = MutableLiveData<List<SearchData>>()
    val retrieved: LiveData<List<SearchData>> get() = _retrieved

    //시작하면 데이터 받아오기
    init {
        setData(createList())
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
            if (v.id == p0 || v.name == p0) {
                list.add(
                    SearchData(
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
    private fun createList(): List<SearchData> {
        val list: MutableList<SearchData> = mutableListOf()
        for (i in 0..30) {
            list.add(
                SearchData(
                    id = "$i",
                    name = "사용자$i",
                    R.drawable.profile
                )
            )
            list.add(
                SearchData(
                    id = "$i",
                    name = "사용자$i",
                    R.drawable.profile
                )
            )
        }
        return list
    }
}