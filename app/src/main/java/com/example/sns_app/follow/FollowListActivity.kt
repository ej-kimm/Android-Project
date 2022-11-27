package com.example.sns_app.follow


import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sns_app.home.FollowDto
import com.example.sns_app.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FollowListActivity : AppCompatActivity() {

    private val followAdapter = FollowAdapter()
    private lateinit var followerRecycler: RecyclerView
    private val followViewModel by viewModels<FollowViewModel>()

    var db: FirebaseFirestore = Firebase.firestore
    private val currentUid = Firebase.auth.currentUser!!.uid


    private val userInformationRef =  db.collection("follow").document(Firebase.auth.currentUser!!.uid)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_followlist)

        val myName = findViewById<TextView>(R.id.textView3)
        val back = findViewById<Button>(R.id.back_btn)
        val followerBtn = findViewById<Button>(R.id.follower_btn)
        val followingBtn = findViewById<Button>(R.id.following_btn)


        followerRecycler = findViewById(R.id.recyclerView_f)


        //팔로워 수
        db.collection("follow").document(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {
            val followDto = it.toObject(FollowDto::class.java)
            val followerCount = followDto?.followers?.keys?.size
            followerBtn.text = "팔로워 "+followerCount.toString()
        }
        //팔로잉 수
        db.collection("follow").document(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {
            val followDto = it.toObject(FollowDto::class.java)
            val followingCount = followDto?.followings?.keys?.size
            followingBtn.text = "팔로잉 "+followingCount.toString()
        }

        //현재 유저의 이름 가져오기
        db.collection("usersInformation").document(currentUid).get().addOnSuccessListener {
            myName.text = it.get("name").toString()
        }


        initFollowerRecyclerView()
        requestFollowingList()

        //뒤로 가기 버튼
        back.setOnClickListener {
            finish()
        }

        followViewModel.follower.observe(this) {
            followAdapter.setFollowList(it)
        }

        followerBtn.setOnClickListener {
            requestFollowerList()
        }
        followingBtn.setOnClickListener {
            requestFollowingList()
            //findViewById<Button>(R.id.button2).text = "팔로잉"
        }
    }



    private fun initFollowerRecyclerView() {
        followerRecycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = followAdapter
        }
    }

    private fun requestFollowerList() {
        val followerList: MutableList<FollowData> = mutableListOf()
        userInformationRef.get().addOnSuccessListener { it1 ->
            val followDto = it1.toObject(FollowDto::class.java)
            followDto?.followers?.keys?.forEach { followerUID ->
                val user = db.collection("usersInformation").document(followerUID)
                user.get().addOnSuccessListener {
                    val name = it.data?.get("name").toString()
                    val profileImage = it.data?.get("profileImage").toString()
                    followerList.add(FollowData(name, profileImage,0))
                    followViewModel.setFollowList(followerList)
                }
            }
        }
    }

    private fun requestFollowingList() {
        val followerList: MutableList<FollowData> = mutableListOf()
        userInformationRef.get().addOnSuccessListener { it1 ->
            val followDto = it1.toObject(FollowDto::class.java)
            followDto?.followings?.keys?.forEach { followerUID ->
                val user = db.collection("usersInformation").document(followerUID)
                user.get().addOnSuccessListener {
                    val name = it.data?.get("name").toString()
                    val profileImage = it.data?.get("profileImage").toString()
                    followerList.add(FollowData(name, profileImage,1))
                    followViewModel.setFollowList(followerList)
                }
            }
        }
    }
}