package com.example.sns_app

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class FollowingAdapter(private val context: Context): RecyclerView.Adapter<FollowingAdapter.ViewHolder>() {

    var datas = mutableListOf<FollowerData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int) : ViewHolder{
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_follower,parent,false)
        return ViewHolder(view)
    }
    override fun getItemCount() : Int = datas.size

    override fun onBindViewHolder(holder : ViewHolder,position:Int){
        holder.bind(datas[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val txtName: TextView = itemView.findViewById(R.id.followerName)
        private val imgProfile: ImageView = itemView.findViewById(R.id.imageView)

        fun bind(item: FollowerData) {
            txtName.text = item.name
            Glide.with(itemView).load(item.img).into(imgProfile)
        }
    }
}