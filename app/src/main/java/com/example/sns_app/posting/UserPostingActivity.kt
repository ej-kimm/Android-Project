package com.example.sns_app.posting

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.sns_app.databinding.ActivityUserPostingBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.*

class UserPostingActivity : AppCompatActivity() {
    lateinit var binding: ActivityUserPostingBinding
    private lateinit var storage: FirebaseStorage
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore
    private val postingCollectionRef = db.collection("posting")
    private var uri: Uri? = null

    companion object {
        const val REQUEST_CODE = 1
        const val UPLOAD_FOLDER = "PostingImage/"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserPostingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        storage = Firebase.storage
        auth = Firebase.auth

        val imageURL = intent.getStringExtra("imageURL").toString()
        val context = intent.getStringExtra("context").toString()
        val postUid = intent.getStringExtra("postUid").toString()

        binding.editText.setText(context) // 수정할 context 보여주기
        binding.editMaxText.text = "(${context.length}/100)"

        val postImgRef = storage.getReference("PostingImage/$imageURL")
        postImgRef.getBytes(Long.MAX_VALUE).addOnSuccessListener {
            val bmp = BitmapFactory.decodeByteArray(it, 0, it.size)
            binding.editImage.setImageBitmap(bmp) // 수정할 image 보여주기
        }

        // 갤러리에서 사진 선택
        binding.editImage.setOnClickListener { // 사진 눌렀을 때 사진 선택 가능
            choosePictures()
        }
        binding.editImageText.setOnClickListener { // 텍스트 눌렀을 때 사진 선택 가능
            choosePictures()
        }

        // 실시간 글자수 검색
        binding.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val input = binding.editText.text.toString()
                binding.editMaxText.text = "(${input.length}/100)"
            }
            override fun afterTextChanged(s: Editable) {}
        })

        // 수정 버튼 누르면 수정 완료
        binding.btnEdit.setOnClickListener {
            // (변경한 context, postUid, currentPostImageRef, currentImageURL)
            if(uploadFile(binding.editText.text, postUid, postImgRef, imageURL))
                finish() // 창 닫기
        }
    }

    // 스토리지에 파일 업로드
    private fun uploadFile(
        context: Editable,
        postUid: String,
        postImgRef: StorageReference,
        imageURL: String
    ): Boolean {
        val storageRef = storage.reference // reference to root
        val time = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.KOREA).format(Date())
        val file = "IMAGE_$time.png"
        val imageRef = storageRef.child(UPLOAD_FOLDER).child(file)

        postImgRef.delete() // storage에서 해당 사진 삭제
        uri?.let { it ->
            imageRef.putFile(it).addOnCompleteListener {
                if (it.isSuccessful) {
                    imageRef.downloadUrl.addOnCompleteListener { uri ->
                        val url = file // 이미지 URL
                        postingCollectionRef.document(postUid).update("context", "$context") // context 수정
                        postingCollectionRef.document(postUid).update("imageURL", url) // imageURL 수정
                    }
                    Snackbar.make(binding.root, "업로드를 완료했습니다", Snackbar.LENGTH_SHORT).show()
                } else if (it.isCanceled) {
                    Snackbar.make(binding.root, "업로드를 실패했습니다", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
        return true
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            uri = result.data?.data
            binding.editImage.setImageURI(uri)
        } else {
//            finish()
        }
    }

    // 갤러리에서 이미지 선택
    private fun choosePictures() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            intent.type = "image/*"
            launcher.launch(intent)
        } else {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE)
        }
    }

}