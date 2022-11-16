package com.example.sns_app

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.sns_app.databinding.HomeFragmentBinding
import com.example.sns_app.databinding.MypageFragmentBinding
import com.example.sns_app.databinding.SearchLayoutBinding

class HomeFragment : Fragment(R.layout.home_fragment) { // 홈 프레그먼트
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
        val binding = HomeFragmentBinding.bind(view)

        // 하단의 코드는 UI 구성 확인을 위한 테스트 코드임
        val viewModel : TestViewModel by viewModels()

        binding.homeRecyclerview.adapter = MyAdapter(viewModel)
        binding.homeRecyclerview.layoutManager = LinearLayoutManager(activity)
        binding.homeRecyclerview.setHasFixedSize(true) // same height

    }
}

class SearchFragment : Fragment(R.layout.search_layout) { // 테스트 프레그먼트, 검색 프레그먼트
    lateinit var searchAdapter : SearchAdapter
    private val data = mutableListOf<SearchData>()
    private val disList = mutableListOf<SearchData>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
        val binding = SearchLayoutBinding.bind(view)

        var recyclerView = binding.recyclerView

        searchAdapter = SearchAdapter(this)
        recyclerView.adapter = searchAdapter
        recyclerView.addItemDecoration(VerticalItemDecorator(10))
        recyclerView.addItemDecoration(HorizontalItemDecorator(10))

        data.apply {
            add(SearchData(id="jjanggu", name="짱구", img=R.drawable.profile))
            add(SearchData(id="chulsu", name="철수", img=R.drawable.profile))
            add(SearchData(id="manggu", name="맹구", img=R.drawable.profile))
            searchAdapter.datas = data
            searchAdapter.notifyDataSetChanged()
        }
    }
}

class PostFragment : Fragment(R.layout.fragment2_layout) { // 테스트 프레그먼트, 게시글 추가 프레그먼트
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // val binding = FragmentLayoutBinding.bind(view) //에러나서 잠시 주석처리

    }
}


class MyPageFragment : Fragment(R.layout.mypage_fragment) { // 마이페이지 프레그먼트
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
        val binding = MypageFragmentBinding.bind(view)
        
        // 하단의 코드는 UI 구성 확인을 위한 테스트 코드임
        binding.postCount.text = "1"
        binding.followerCount.text = "2"
        binding.followingCount.text = "10"
        binding.mypageMyImg.setImageResource(R.drawable.ic_baseline_person_24)

        //임시적으로 "팔로잉" textView를 클릭하면 followList로 이동
        binding.following.setOnClickListener{
            val intent = Intent(context,FollowListActivity::class.java)
            startActivity(intent)
        }

        val viewModel : TestViewModel by viewModels()

        binding.mypageRecyclerview.adapter = MyAdapter(viewModel)
        binding.mypageRecyclerview.layoutManager = LinearLayoutManager(activity)
        binding.mypageRecyclerview.setHasFixedSize(true) // same height
    }
}