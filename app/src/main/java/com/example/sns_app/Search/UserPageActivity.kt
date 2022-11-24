package com.example.sns_app.Search

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.sns_app.Home.FollowDto
import com.example.sns_app.R
import com.example.sns_app.databinding.ActivityUserpageBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage


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

        //검색한 유저 아이디 받아오기
        val searchUID = intent.getStringExtra("data")
        checkFollow(searchUID) // 해당 유저를 팔로우 했는지 체크

        val my_id = binding.myId
        val my_img = binding.mypageMyImg
        val follower = binding.followerCount
        val following = binding.followingCount
        val postCount = binding.postCount

        binding.followBtn.setOnClickListener {
            // 팔로우 버튼을 누르면
            // 상대방 팔로워 목록에 내 uid : true
            val targetUser = db.collection("follow").document(searchUID!!)
            // transaction, db 데이터 저장
            db.runTransaction {
                // it : Transaction
                // it.get(Document) : 해당 Document 받아오기
                // it.set(Document, Dto 객체) : 해당 Document에 Dto 객체 저장하기
                var followDto = it.get(targetUser).toObject(FollowDto::class.java)
                if (followDto == null) {
                    followDto = FollowDto().apply {
                        followers[currentUid] = true
//                        notifyFollow()
                    }
                } else {
                    with(followDto) {
                        if (followers.containsKey(currentUid)) {
                            // 언팔로우
                            followers.remove(currentUid)
                        } else {
                            // 팔로우
                            followers[currentUid] = true
//                            notifyFollow()
                        }
                    }
                }
                it.set(targetUser, followDto)
                return@runTransaction
            }
            // 내 팔로잉 목록에 상대방 uid : true
            val meUser = db.collection("follow").document(currentUid)
            db.runTransaction {
                var followDto = it.get(meUser).toObject(FollowDto::class.java)
                if (followDto == null) {
                    followDto = FollowDto().apply {
                        followings[searchUID.toString()] = true
                        binding.followBtn.text = "언팔로우"
//                        notifyFollow()
                    }
                } else {
                    with(followDto!!) {
                        if (followings.containsKey(searchUID.toString())) {
                            // 언팔로우
                            followings.remove(searchUID.toString())
                            binding.followBtn.text = "팔로우"
                        } else {
                            // 팔로우
                            followings.set(searchUID.toString(), true)
                            binding.followBtn.text = "언팔로우"
//                            notifyFollow()
                        }
                    }
                }
                it.set(meUser, followDto!!)
                return@runTransaction
            }
        }

        //검색한 유저의 id, 이미지, 게시물 개수, 팔로잉수, 팔로워수 띄우기
        db.collection("usersInformation")
            .document(searchUID!!) // 전달받은 uid로 정보 받아오기
            .get()
            .addOnSuccessListener {
                profileImgRef = storage.getReference("ProfileImage/${it["profileImage"].toString()}")
                //검색된 유저 id
                my_id.text = it["userID"].toString()
                //검색된 유저 이미지
                if(it["profileImage"].toString().equals("default")) {
                    Glide.with(this).load(R.drawable.profile).apply(RequestOptions.circleCropTransform()).into(my_img)
                } else {
                    profileImgRef.getBytes(Long.MAX_VALUE).addOnSuccessListener {
                        val bmp = BitmapFactory.decodeByteArray(it, 0, it.size)
                        Glide.with(my_img).load(bmp).apply(RequestOptions.circleCropTransform()).into(my_img)
                    }
                }
                //게시물 개수(임시)
                postCount.text = "0"
                //팔로잉 수(임시)
                following.text = "4"
                //팔로워 수(임시)
                follower.text = "3"
            }


        val viewModel : UserPageViewModel  by viewModels()

        viewModel.createList(searchUID)
        viewModel.posts.observe(this) {
            searchUserAdapter.setDataList(it)
        }

        searchUserAdapter = SearchUserAdapter(viewModel)
        binding.recyclerSearchUser.adapter = searchUserAdapter
        binding.recyclerSearchUser.layoutManager = LinearLayoutManager(this)
        binding.recyclerSearchUser.setHasFixedSize(true) // same height

    }

    private fun checkFollow(searchUID: String?) {
        db.collection("follow").document(currentUid).get().addOnSuccessListener {
            val followDto = it.toObject(FollowDto::class.java)
            if (followDto != null) {
                if(followDto.followings.get(searchUID) == true) { // 해당 유저를 팔로우한 상태라면
                    binding.followBtn.text = "언팔로우" // 버튼의 Text를 언팔로우로 변경
                }
            }
        }
    }

}