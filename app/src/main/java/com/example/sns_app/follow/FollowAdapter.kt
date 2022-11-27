package com.example.sns_app.follow

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sns_app.R

import android.graphics.BitmapFactory
import android.widget.Button
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.storage.FirebaseStorage

class FollowAdapter :
    RecyclerView.Adapter<FollowAdapter.ViewHolder>() {
    private var items: List<FollowData> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_follower, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val txtName: TextView = itemView.findViewById(R.id.followerName)
        private val imgProfile: ImageView = itemView.findViewById(R.id.imageView)


        fun bind(item: FollowData) {

            val storage: FirebaseStorage = FirebaseStorage.getInstance()
            val profileImgRef = storage.getReference("ProfileImage/${item.img}")

            if(item.state==0){
                itemView.findViewById<Button>(R.id.button2).text="팔로워"
            }else if(item.state==1){
                itemView.findViewById<Button>(R.id.button2).text="팔로잉"
            }

            //사진 설정
            if(item.img.contains("default")) {
                Glide.with(itemView).load(R.drawable.profile)
                    .apply(RequestOptions.circleCropTransform()).into(imgProfile)
            } else {
                profileImgRef.getBytes(Long.MAX_VALUE).addOnSuccessListener {
                    val bmp = BitmapFactory.decodeByteArray(it, 0, it.size)
                    Glide.with(itemView).load(bmp).apply(RequestOptions.circleCropTransform()).into(imgProfile)
                }
            }
            //이름 설정
            txtName.text = item.name

        }

    }

    fun setFollowList(list: List<FollowData>) {

        items = list
        notifyDataSetChanged()
    }



}