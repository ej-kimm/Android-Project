package com.example.sns_app.Search

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.sns_app.R
import com.example.sns_app.databinding.ActivityUserpageBinding
import com.example.sns_app.databinding.RecyclerSearchBinding
import com.example.sns_app.myPage.MyPageAdapter
import com.example.sns_app.myPage.MyPageViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import org.w3c.dom.Text


class UserPageActivity : AppCompatActivity(){

    private var db : FirebaseFirestore = Firebase.firestore
    private val storage = Firebase.storage
    private val currentUid = Firebase.auth.currentUser!!.uid
    lateinit var searchUserAdapter: SearchUserAdapter
    lateinit var binding: ActivityUserpageBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserpageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var profileImgRef : StorageReference
        var recy = findViewById<RecyclerView>(R.id.recycler_searchUser)

        //검색한 유저 아이디 받아오기
        val searchId = intent.getStringExtra("data")



        val my_id = findViewById<TextView>(R.id.my_id)
        val my_img = findViewById<ImageView>(R.id.mypage_my_img)
        val follower = findViewById<TextView>(R.id.follower_count)
        val following = findViewById<TextView>(R.id.following_count)
        val postCount = findViewById<TextView>(R.id.post_count)

        //검색한 유저의 id, 이미지, 게시물 개수, 팔로잉수, 팔로워수 띄우기
        db.collection("usersInformation")
            .whereEqualTo("userID",searchId)
            .get()
            .addOnSuccessListener { document ->
                for (doc in document) {
                    profileImgRef =
                        storage.getReference("ProfileImage/${doc["profileImage"].toString()}")
                    //검색된 유저 id
                    my_id.text = doc["userID"].toString()
                    //검색된 유저 이미지
                    profileImgRef.getBytes(Long.MAX_VALUE).addOnSuccessListener {
                        val bmp = BitmapFactory.decodeByteArray(it, 0, it.size)
                        Glide.with(my_img).load(bmp).apply(RequestOptions.circleCropTransform())
                            .into(my_img)
                    }
                    //게시물 개수(임시)
                    postCount.text = "0"
                    //팔로잉 수(임시)
                    following.text = "4"
                    //팔로워 수(임시)
                    follower.text = "3"
                }
            }



        var searchUser = db.collection("userInformation").whereEqualTo("userID",searchId).get()


//        val list = ArrayList<SearchUserData>()
//        //recyclerView에 넣을 검색 유저 아이디1, 검색 유저 아이디 2, 검색 유저 프로필 사진, 포스팅사진, 포스팅컨텍스트, 자신 프로필 사진
//        db.collection("posting")
//            .whereEqualTo("userID",searchId)
//            .get()
//            .addOnSuccessListener { document ->
//                for(doc in document){
//                        list.add(
//                            SearchUserData(
//                                searchUserId = doc["userId"].toString(),
//                                searchUserId2 = doc["userId"].toString(),
//                                searchUserComment = doc["context"].toString(),
//                                my_img ="C:\\Users\\kwon\\Desktop\\end\\Android-Project\\app\\src\\main\\res\\drawable\\profile.png",
//                                searchUser_img = "C:\\Users\\kwon\\Desktop\\end\\Android-Project\\app\\src\\main\\res\\drawable\\profile.png",
//                                searchUserPost_img = "C:\\Users\\kwon\\Desktop\\end\\Android-Project\\app\\src\\main\\res\\drawable\\profile.png"))
//
//                    }
//            }

        val viewModel : UserPageViewModel  by viewModels()

        viewModel.posts.observe(this) {
            searchUserAdapter.setDataList(it)
        }

        searchUserAdapter = SearchUserAdapter(viewModel)
        binding.recyclerSearchUser.adapter = searchUserAdapter
        binding.recyclerSearchUser.layoutManager = LinearLayoutManager(this)
        binding.recyclerSearchUser.setHasFixedSize(true) // same height





    }

}