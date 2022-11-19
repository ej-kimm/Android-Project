package com.example.sns_app.myPage

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sns_app.Follow.FollowListActivity
import com.example.sns_app.Home.MyAdapter
import com.example.sns_app.R
import com.example.sns_app.TestViewModel
import com.example.sns_app.databinding.MypageFragmentBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class MyPageFragment : Fragment(R.layout.mypage_fragment) { // 마이페이지 프레그먼트
    lateinit var storage: FirebaseStorage
    lateinit var binding: MypageFragmentBinding
    private val db: FirebaseFirestore = Firebase.firestore
    private val usersInformationRef = db.collection("usersInformation")
    private val currentUid = Firebase.auth.currentUser!!.uid
    private var filename = ""

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri -> // 이미지 선택 후
        if (uri != null) { // 선택된 이미지가 존재한다면
            filename = currentUid // 자신의 uid를 파일 이름으로
            val imageRef = storage.reference.child("ProfileImage/${filename}") // 파일 이름으로 스토리지 참조 획득
            imageRef.putFile(uri).addOnCompleteListener { // 선택된 이미지를 획득한 참조에 저장
                if (it.isSuccessful) {
                    // upload success
                    Snackbar.make(binding.root, "변경이 완료되었습니다.", Snackbar.LENGTH_SHORT).show()
                    usersInformationRef.document(currentUid).update("profileImage", filename).addOnSuccessListener {
                        updateProfileImage() // 변경 성공 시 변경된 이미지로 UI update
                    }
                }
            }
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
        binding = MypageFragmentBinding.bind(view)
        updateProfileImage() // 초기 화면 구성 시 이미지 로딩

        // 하단의 코드는 UI 구성 확인을 위한 테스트 코드임
        binding.postCount.text = "1"
        binding.followerCount.text = "2"
        binding.followingCount.text = "10"

        binding.editProfile.setOnClickListener { // 프로필 편집 버튼을 누르면
            // 사진 선택 창 실행
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        //임시적으로 "팔로잉" textView를 클릭하면 followList로 이동
        binding.following.setOnClickListener{
            val intent = Intent(context, FollowListActivity::class.java)
            startActivity(intent)
        }

        val viewModel : TestViewModel by viewModels()

        binding.mypageRecyclerview.adapter = MyAdapter(viewModel)
        binding.mypageRecyclerview.layoutManager = LinearLayoutManager(activity)
        binding.mypageRecyclerview.setHasFixedSize(true) // same height
    }

    private fun updateProfileImage() {
        storage = Firebase.storage
        usersInformationRef.document(currentUid).get().addOnSuccessListener { // 유저 정보 받아오기
            binding.myId.text = it["name"].toString() // 이름은 myId에
            filename = it["profileImage"].toString() // 파일 이름을 받아와서
            if(it["profileImage"].toString() == "default") { // profileImage 필드의 값이 default라면
                binding.mypageMyImg.setImageResource(R.drawable.profile) // default 프로필 이미지로 변경
            } else {
                val profileImgRef = storage.getReference("ProfileImage/${filename}") // 유저 정보의 파일 정보 참조 획득
                displayImageRef(profileImgRef, binding.mypageMyImg)
            }
        }
    }

    private fun displayImageRef(imageRef: StorageReference?, view: ImageView) { // 이미지를 화면에 띄움
        imageRef?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
            val bmp = BitmapFactory.decodeByteArray(it, 0, it.size)
            view.setImageBitmap(bmp)
        }?.addOnFailureListener {
            // Failed to download the image
        }
    }
}