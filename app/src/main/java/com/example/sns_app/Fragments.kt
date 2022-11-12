package com.example.sns_app

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.TestViewModel
import com.example.sns_app.databinding.FragmentLayoutBinding
import com.example.sns_app.databinding.HomeFragmentBinding
import com.example.sns_app.databinding.MypageFragmentBinding

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

class SearchFragment : Fragment(R.layout.fragment_layout) { // 테스트 프레그먼트, 검색 프레그먼트
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentLayoutBinding.bind(view)
        binding.textView.text = "SearchFragment"
    }
}

class PostFragment : Fragment(R.layout.fragment_layout) { // 테스트 프레그먼트, 게시글 추가 프레그먼트
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentLayoutBinding.bind(view)
        binding.textView.text = "PostFragment"
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

        val viewModel : TestViewModel by viewModels()

        binding.mypageRecyclerview.adapter = MyAdapter(viewModel)
        binding.mypageRecyclerview.layoutManager = LinearLayoutManager(activity)
        binding.mypageRecyclerview.setHasFixedSize(true) // same height
    }
}