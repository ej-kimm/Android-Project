package com.example.sns_app.Follow


import android.annotation.SuppressLint
import android.graphics.Insets.add
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.sns_app.Home.FollowDto
import com.example.sns_app.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FollowListActivity : AppCompatActivity() {

    lateinit var followerAdapter : FollowerAdapter
    lateinit var followingAdapter : FollowingAdapter
    private val data = mutableListOf<FollowerData>()

    var db : FirebaseFirestore = Firebase.firestore
    private val currentUid = Firebase.auth.currentUser!!.uid
    //var followersList = mutableListOf<String>()

    // 팔로우 컬렉션에서 현재 접속한 uid document 참조 획득
    private val userInformationRef = db.collection("follow").document(Firebase.auth.currentUser!!.uid)
    private val itemRef = db.collection("userInformation")

   // private val currentUser = db.collection("follow")?.document(this.currentUid)

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_followlist)



        val followerBtn = findViewById<Button>(R.id.follower_btn)
        val followingBtn = findViewById<Button>(R.id.following_btn)
        val back = findViewById<Button>(R.id.back_btn)

        back.setOnClickListener {
            finish()
        }
        showFragmentFollower()
        followerBtn.setOnClickListener{
            showFragmentFollower()
        }
        followingBtn.setOnClickListener {
            showFragmentFollowing()
        }
    }
    fun showFragmentFollower(){
        val followerRecycler = findViewById<RecyclerView>(R.id.recyclerView_f)

        followerAdapter = FollowerAdapter(this)
        followerRecycler.adapter = followerAdapter


        userInformationRef.get().addOnSuccessListener { it1 ->
            val followDto = it1.toObject(FollowDto::class.java) // 획득한 snapshot을 받아와 데이터 클래스로 형 변환
            followDto?.followers?.keys?.forEach { followerUID -> // 팔로워 목록의 키 ( uid ) 를 루프로 돌며
                itemRef.get().addOnSuccessListener {
                    data.apply {
                        for(doc in it){
                            if(doc.id == followerUID){
                                    add(
                                        FollowerData(
                                            name = doc["name"].toString(),
                                            img = R.drawable.profile
                                        )
                                    )
                                    followerAdapter.datas = data
                                    followerAdapter.notifyDataSetChanged()
                                }
                            }

                        }

                }
            }
        }



        /*data.apply {
            data.clear()
            add(FollowerData(name="짱구", img= R.drawable.profile))
            add(FollowerData(name="철수", img= R.drawable.profile))
            add(FollowerData(name="유리", img= R.drawable.profile))
            add(FollowerData(name="길동", img= R.drawable.profile))
            followerAdapter.datas = data
            followerAdapter.notifyDataSetChanged()
        }*/
    }

    fun showFragmentFollowing(){
        val followingRecycler = findViewById<RecyclerView>(R.id.recyclerView_f)

        followingAdapter = FollowingAdapter(this)
        followingRecycler.adapter = followerAdapter
        /*data.apply {
            data.clear()
            add(FollowerData(name="김희철", img= R.drawable.profile))
            add(FollowerData(name="강호동", img= R.drawable.profile))
            add(FollowerData(name="서장훈", img= R.drawable.profile))
            add(FollowerData(name="이수근", img= R.drawable.profile))
            add(FollowerData(name="이상민", img= R.drawable.profile))
            add(FollowerData(name="김영철", img= R.drawable.profile))
            followingAdapter.datas = data
            followingAdapter.notifyDataSetChanged()
        }*/
    }
}