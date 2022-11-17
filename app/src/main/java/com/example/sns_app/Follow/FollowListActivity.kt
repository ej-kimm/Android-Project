package com.example.sns_app.Follow


import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.sns_app.R

class FollowListActivity : AppCompatActivity() {
    lateinit var followerAdapter : FollowerAdapter
    lateinit var followingAdapter : FollowingAdapter
    private val data = mutableListOf<FollowerData>()

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
        data.apply {
            data.clear()
            add(FollowerData(name="짱구", img= R.drawable.profile))
            add(FollowerData(name="철수", img= R.drawable.profile))
            add(FollowerData(name="유리", img= R.drawable.profile))
            add(FollowerData(name="길동", img= R.drawable.profile))
            followerAdapter.datas = data
            followerAdapter.notifyDataSetChanged()
        }
    }

    fun showFragmentFollowing(){
        val followingRecycler = findViewById<RecyclerView>(R.id.recyclerView_f)

        followingAdapter = FollowingAdapter(this)
        followingRecycler.adapter = followerAdapter
        data.apply {
            data.clear()
            add(FollowerData(name="김희철", img= R.drawable.profile))
            add(FollowerData(name="강호동", img= R.drawable.profile))
            add(FollowerData(name="서장훈", img= R.drawable.profile))
            add(FollowerData(name="이수근", img= R.drawable.profile))
            add(FollowerData(name="이상민", img= R.drawable.profile))
            add(FollowerData(name="김영철", img= R.drawable.profile))
            followingAdapter.datas = data
            followingAdapter.notifyDataSetChanged()
        }
    }
}