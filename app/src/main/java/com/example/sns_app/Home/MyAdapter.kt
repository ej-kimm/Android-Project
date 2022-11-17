package com.example.sns_app.Home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sns_app.R
import com.example.sns_app.TestViewModel
import com.example.sns_app.databinding.PostLayoutBinding

// 리사이클러뷰 어댑터
class MyAdapter(private val viewModel: TestViewModel) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
        inner class ViewHolder(private val binding: PostLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setContents(pos: Int) {

            // 하단의 코드는 UI 구성 확인을 위한 테스트 코드임
            val item = viewModel.items[pos]
            binding.publisherId.text = item.publisher_id
            binding.publisherId2.text = item.publisher_id
            binding.postContent.text = item.post_content
            binding.postImg.setImageResource(R.drawable.ic_launcher_background)
            binding.publisherImg.setImageResource(R.drawable.ic_baseline_person_24)
            binding.myImg.setImageResource(R.drawable.ic_baseline_person_24)

//            binding.commentbtn.setOnClickListener {
//                println("Comment Button Clicked !!$pos")
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = PostLayoutBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setContents(position)
    }

    override fun getItemCount() = viewModel.items.size
}