package com.example.sns_app

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sns_app.Follow.FollowListActivity
import com.example.sns_app.Home.MyAdapter
import com.example.sns_app.Search.HorizontalItemDecorator
import com.example.sns_app.Search.SearchAdapter
import com.example.sns_app.Search.SearchViewModel
import com.example.sns_app.Search.VerticalItemDecorator

import com.example.sns_app.databinding.HomeFragmentBinding
import com.example.sns_app.databinding.SearchLayoutBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

// 게시글 기능 구현 이후 분리

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
    lateinit var searchAdapter: SearchAdapter
    private val searchViewModel by viewModels<SearchViewModel>()
    //private val data = mutableListOf<SearchData>()
    //private val disList = mutableListOf<SearchData>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = SearchLayoutBinding.bind(view)
        val recyclerView = binding.recyclerView

        //키보드가 recyclerview랑 같이 안올라가도록
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        //searchView 에서 검색되는 단어 검색
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                TODO("Not yet implemented")
            }
            override fun onQueryTextChange(p0: String?): Boolean {
                // text 변경될 때마다 불리는 콜백 함수
                if (p0 == "") {
                    searchViewModel.emptyInput()
                } else if (p0 != null) {
                    searchViewModel.searchingList(p0)
                }
                return false
            }
        })
        //검색되는 단어가 있는지 observe
        searchViewModel.retrieved.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                searchAdapter.setSearchDataList(emptyList())
                return@observe
            }
            searchAdapter.setSearchDataList(it)
        }
        //검색 x
        searchViewModel.searchData.observe(viewLifecycleOwner) {
            searchAdapter.setSearchDataList(it)
        }

        searchAdapter = SearchAdapter(this)
        recyclerView.adapter = searchAdapter
        recyclerView.addItemDecoration(VerticalItemDecorator(10))
        recyclerView.addItemDecoration(HorizontalItemDecorator(10))
    }
}

class PostFragment : Fragment(R.layout.fragment2_layout) { // 테스트 프레그먼트, 게시글 추가 프레그먼트
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // val binding = FragmentLayoutBinding.bind(view) //에러나서 잠시 주석처리

    }
}