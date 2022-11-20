package com.example.sns_app

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sns_app.Home.MyAdapter
import com.example.sns_app.Posting.PostingData
import com.example.sns_app.Search.HorizontalItemDecorator
import com.example.sns_app.Search.SearchAdapter
import com.example.sns_app.Search.SearchViewModel
import com.example.sns_app.Search.VerticalItemDecorator
import com.example.sns_app.databinding.HomeFragmentBinding
import com.example.sns_app.databinding.SearchLayoutBinding
import com.example.sns_app.databinding.UserpostingFramentBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.*


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

// 게시글 추가 프레그먼트
class PostFragment : Fragment(R.layout.userposting_frament) {
    lateinit var binding: UserpostingFramentBinding
    private lateinit var storage: FirebaseStorage
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore
    private val postingCollectionRef = db.collection("posting")
    private val usersInformationRef = db.collection("usersInformation")
    private var uri: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = UserpostingFramentBinding.bind(view)
        storage = Firebase.storage
        auth = Firebase.auth

        // 갤러리에서 사진 선택
        binding.uploadImage.setOnClickListener { // 사진 눌렀을 때 사진 선택 가능
            choosePictures()
        }
        binding.textView5.setOnClickListener { // 텍스트 눌렀을 때 사진 선택 가능
            choosePictures()
        }

        // 실시간 글자수 검색
        binding.uploadText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val input = binding.uploadText.text.toString()
                binding.maxText.text = "(${input.length}/100)"
            }
            override fun afterTextChanged(s: Editable) {}
        })

        // 공유 버튼 누르면 사진 업로드 완료
        binding.btnUpload.setOnClickListener {
            uploadFile(binding.uploadText.text)
            binding.uploadText.setText("") // 업로드 완료. 텍스트 초기화
            binding.uploadImage.setImageResource(R.drawable.select) // 업로드 완료. 이미지뷰 초기화
        }
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            uri = result.data?.data
            binding.uploadImage.setImageURI(uri)
        } else {
//            finish()
        }
    }

    // 갤러리에서 이미지 선택
    private fun choosePictures() {
        if (activity?.let {
                ContextCompat.checkSelfPermission(it, Manifest.permission.READ_EXTERNAL_STORAGE)
            } == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            intent.type = "image/*"
            launcher.launch(intent)
        } else {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        }
    }

    // 스토리지에 파일 업로드
    private fun uploadFile(context: Editable) {
        val storageRef = storage.reference // reference to root
        var profileURL = ""
        usersInformationRef.document(auth.currentUser!!.uid).get().addOnSuccessListener { // 유저 정보 받아오기
            val filename = it["profileImage"].toString() // 파일 이름을 받아와서
            profileURL = filename
        }

        val time = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var file = "IMAGE_$time.png"
        val imageRef = storageRef.child(UPLOAD_FOLDER).child(file)
        uri?.let { it ->
            imageRef.putFile(it).addOnCompleteListener {
                if (it.isSuccessful) {
                    imageRef.downloadUrl.addOnCompleteListener { uri ->
                        val url = file // 이미지 URL
                        val userUID = auth.currentUser?.uid.toString() // 작성자 UID
                        val userID = auth.currentUser?.email.toString().split("@")[0] // 작성자 ID
                        val currentTime = System.currentTimeMillis().toString() // 현재 시간 (출력할 때 형식 필요)
                        val postingData =
                            PostingData(context.toString(), url, userUID, userID, profileURL, currentTime) // posting정보를 담은 data class에 저장
                        postingCollectionRef.document().set(postingData)
                    }
                    Snackbar.make(binding.root, "업로드를 완료했습니다", Snackbar.LENGTH_SHORT).show()
                } else if (it.isCanceled) {
                    Snackbar.make(binding.root, "업로드를 실패했습니다", Snackbar.LENGTH_SHORT).show()
                }
            }
        }

    }

    companion object {
        const val UPLOAD_FOLDER = "PostingImage/"
    }
}