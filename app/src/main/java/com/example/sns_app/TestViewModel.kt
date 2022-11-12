package com.example.myapplication

import androidx.lifecycle.ViewModel

// 더미 데이터 클래스
data class Item(val publisher_id: String, val post_content: String)

class TestViewModel : ViewModel() {
    val items = ArrayList<Item>()

    init { // 더미 데이터
        addItem(Item("changuen","First post!!!"))
        addItem(Item("changuen","Second post!!!"))
        addItem(Item("changuen","Third post!!!"))
        addItem(Item("changuen","Fourth post!!!"))
    }

    private fun addItem(item: Item){
        items.add(item)
    }

    fun updateItem(item: Item, pos:Int) {
        items[pos] = item
    }

    fun deleteItem(pos: Int) {
        items.removeAt(pos)
    }
}