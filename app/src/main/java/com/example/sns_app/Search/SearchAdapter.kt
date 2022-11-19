package com.example.sns_app.Search

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.sns_app.R

class SearchAdapter(private val context: Fragment) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private var items: List<SearchData> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_search, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val txtId: TextView = itemView.findViewById(R.id.textView)
        private val txtName: TextView = itemView.findViewById(R.id.followerName)
        private val imgProfile: ImageView = itemView.findViewById(R.id.imageView)
        fun bind(item: SearchData) {
            txtId.text = item.id
            txtName.text = item.name
//            Glide.with(itemView).load(item.img).into(imgProfile)
            item.img?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
                val bmp = BitmapFactory.decodeByteArray(it, 0, it.size)
//                imgProfile.setImageBitmap(bmp)
                Glide.with(itemView).load(bmp).apply(RequestOptions.circleCropTransform()).into(imgProfile)
            } // 참조 활용, 이미지뷰에 이미지 설정

        }
    }

    fun setSearchDataList(list: List<SearchData>) {
        items = list
        notifyDataSetChanged()
    }

}