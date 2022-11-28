package com.example.sns_app.search

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.sns_app.home.FollowDto
import com.example.sns_app.R
import com.example.sns_app.databinding.ActivityUserpageBinding
import com.example.sns_app.myPage.MyPageAdapter
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
    private lateinit var searchUserAdapter: MyPageAdapter
    private lateinit var binding: ActivityUserpageBinding
    private val followInformationRef = db.collection("follow")
    private val postingInformationRef = db.collection("posting")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserpageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // progressBar 추가, 상의 후 다른 View 추가
        val delay = 500L
        Handler(Looper.myLooper()!!).postDelayed({
            binding.userProgressBar.isVisible = false
        }, delay)

        var profileImgRef : StorageReference

        //검색한 유저 아이디 받아오기
        val searchUID = intent.getStringExtra("data")
        checkFollow(searchUID) // 해당 유저를 팔로우 했는지 체크

        if(searchUID != null){
            updateUserInfo(searchUID)
        }

        val my_id = binding.myId
        val my_img = binding.mypageMyImg
        val follower = binding.followerCount
        val following = binding.followingCount
        val postCount = binding.postCount

        binding.followBtn.setOnClickListener {
            // 팔로우 버튼을 누르면
            // 상대방 팔로워 목록에 내 uid : true
            followEvent(searchUID)
        }

        //검색한 유저의 id, 이미지, 게시물 개수, 팔로잉수, 팔로워수 띄우기
        db.collection("usersInformation")
            .document(searchUID!!) // 전달받은 uid로 정보 받아오기
            .get()
            .addOnSuccessListener { it ->
                profileImgRef = storage.getReference("ProfileImage/${it["profileImage"].toString()}")
                //검색된 유저 id
                my_id.text = it["userID"].toString()
                //검색된 유저 이미지
                if(it["profileImage"].toString() == "default") {
                    Glide.with(this).load(R.drawable.profile).apply(RequestOptions.circleCropTransform()).into(my_img)
                } else {
                    profileImgRef.getBytes(Long.MAX_VALUE).addOnSuccessListener {
                        val bmp = BitmapFactory.decodeByteArray(it, 0, it.size)
                        Glide.with(my_img).load(bmp).apply(RequestOptions.circleCropTransform()).into(my_img)
                    }
                }

            }


//        val viewModel : UserPageViewModel  by viewModels()
//
//        viewModel.createList(searchUID)
//        viewModel.posts.observe(this) {
//            searchUserAdapter.setDataList(it)
//        }

        searchUserAdapter = MyPageAdapter(searchUID)
        binding.recyclerSearchUser.adapter = searchUserAdapter
        binding.recyclerSearchUser.layoutManager = LinearLayoutManager(this)
        binding.recyclerSearchUser.setHasFixedSize(true) // same height
    }

    private fun updateUserInfo(searchUID: String) {
        followInformationRef.document(searchUID).addSnapshotListener { snapshot, _ ->
            val followDto = snapshot?.toObject(FollowDto::class.java)
            if (followDto != null) {
                binding.followerCount.text = followDto.followers.size.toString()
                binding.followingCount.text = followDto.followings.size.toString()
                println(followDto.toString())
            }
        }
        postingInformationRef.whereEqualTo("uid",searchUID).get().addOnSuccessListener {
            binding.postCount.text = it?.size().toString()
        }

    }

    private fun followEvent(searchUID: String?) {
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
//                        notifyFollow()
                }
            } else {
                with(followDto!!) {
                    if (followings.containsKey(searchUID.toString())) {
                        // 언팔로우
                        followings.remove(searchUID.toString())
                    } else {
                        // 팔로우
                        followings.set(searchUID.toString(), true)
//                            notifyFollow()
                    }
                }
            }
            it.set(meUser, followDto!!)
            return@runTransaction
        }
    }

    private fun checkFollow(searchUID: String?) {
        db.collection("follow").document(currentUid).addSnapshotListener { snapshot ,_ ->
            val followDto = snapshot?.toObject(FollowDto::class.java)
            if (followDto != null) {
                if(followDto.followings.get(searchUID) == true) { // 해당 유저를 팔로우한 상태라면
                    binding.followBtn.text = "언팔로우" // 버튼의 Text를 언팔로우로 변경
                } else
                    binding.followBtn.text = "팔로우"
            } else {
                binding.followBtn.text = "팔로우"
            }
        }
    }

}