package com.example.sns_app.Search

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.sns_app.R
import org.w3c.dom.Text

class SearchUserAdapter(private val items : ArrayList<SearchUserData>):RecyclerView.Adapter<SearchUserAdapter.ViewHolder>() {


    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder : SearchUserAdapter.ViewHolder,position:Int){

    }

    override fun onCreateViewHolder(parent:ViewGroup,viewType:Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_searchuser,parent,false)
        return ViewHolder(inflatedView)
    }

    inner class ViewHolder(v : View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        fun bind(items: SearchUserData) {
            val searchUserImg: ImageView = itemView.findViewById(R.id.searchUser_img)
            val searchUserId: TextView = itemView.findViewById(R.id.searchUser_id)
            val searchUserPostImg: ImageView = itemView.findViewById(R.id.searchUserPost_img)
            val searchUserId2: TextView = itemView.findViewById(R.id.searchUser_id2)
            val searchUserContent: TextView = itemView.findViewById(R.id.searchUserPost_content)
            val myImg: ImageView = itemView.findViewById(R.id.my_img)
            /*
            //검색한 유저 프로필 사진
            items.searchUserimg.getBytes(Long.MAX_VALUE).addOnSuccessListener {
                val bmp = BitmapFactory.decodeByteArray(it, 0, it.size)
                Glide.with(itemView).load(bmp).apply(RequestOptions.circleCropTransform())
                    .into(searchUserImg)
            }*/
            Glide.with(itemView).load(items.searchUser_img).into(searchUserImg)

            //검색 유저 아이디
            searchUserId.text = items.searchUserId

            /*
            //검색 유저 게시글
            items.searchUserPostImg.getBytes(Long.MAX_VALUE).addOnSuccessListener {
                val bmp = BitmapFactory.decodeByteArray(it, 0, it.size)
                Glide.with(itemView).load(bmp).apply(RequestOptions.circleCropTransform())
                    .into(searchUserPostImg)
            }*/
            Glide.with(itemView).load(items.searchUserPost_img).into(searchUserPostImg)

            //검색 유저 아이디
            searchUserId2.text = items.searchUserId2
            //검색 유저 게시글 코멘트
            searchUserContent.text = items.searchUserComment

            /*
            //나의 이미지
            items.myImg.getBytes(Long.MAX_VALUE).addOnSuccessListener {
                val bmp = BitmapFactory.decodeByteArray(it, 0, it.size)
                Glide.with(itemView).load(bmp).apply(RequestOptions.circleCropTransform())
                    .into(myImg)
            }*/
            Glide.with(itemView).load(items.my_img).into(myImg)
        }



    }

}