package com.example.sns_app.myPage


import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sns_app.Posting.PostingData
import com.example.sns_app.databinding.PostLayoutBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

// 리사이클러뷰 어댑터
class MyPageAdapter(private val viewModel: MyPageViewModel) : RecyclerView.Adapter<MyPageAdapter.ViewHolder>() {
    private var items: List<PostingData> = emptyList()

    inner class ViewHolder(private val binding: PostLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PostingData) {
            val storage = Firebase.storage
            val postImgRef = storage.getReference("PostingImage/${item.imageURL}")

            binding.publisherId.text = item.userID
            binding.publisherId2.text = item.userID
            binding.postContent.text = item.context
            postImgRef.getBytes(Long.MAX_VALUE).addOnSuccessListener {
                val bmp = BitmapFactory.decodeByteArray(it, 0, it.size)
                Glide.with(binding.root).load(bmp).into(binding.postImg)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = PostLayoutBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun setDataList(list: List<PostingData>) {
        items = list
        notifyDataSetChanged()
    }
}