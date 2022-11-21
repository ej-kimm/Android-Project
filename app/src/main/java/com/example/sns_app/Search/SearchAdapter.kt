package com.example.sns_app.Search

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.sns_app.R
import com.example.sns_app.databinding.ActivityMainBinding
import kotlinx.coroutines.NonDisposableHandle.parent
import kotlin.coroutines.coroutineContext
    private lateinit var itemClickListner : SearchAdapter.OnItemClickListner

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
            item.img.getBytes(Long.MAX_VALUE).addOnSuccessListener {
                val bmp = BitmapFactory.decodeByteArray(it, 0, it.size)
//                imgProfile.setImageBitmap(bmp)
                Glide.with(itemView).load(bmp).apply(RequestOptions.circleCropTransform()).into(imgProfile)

                //아이템 클릭 리스너
                itemView.setOnClickListener {
                    val pos = adapterPosition
                    if(pos != RecyclerView.NO_POSITION && itemClickListner!=null){
                        itemClickListner.OnItemClick(itemView,pos)
                    }
                }

            } // 참조 활용, 이미지뷰에 이미지 설정

        }
    }

    fun setSearchDataList(list: List<SearchData>) {
        items = list
        notifyDataSetChanged()
    }
    ///////////////////////////////아이템 클릭
    interface OnItemClickListner{
        fun OnItemClick(view: View, position: Int)
    }
    fun setOnItemClickListner(onItemClickListner: OnItemClickListner){
        itemClickListner = onItemClickListner

    }

}