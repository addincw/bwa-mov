package com.addincendekia.bwa_mov.auth

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.addincendekia.bwa_mov.main.MainActivity
import com.addincendekia.bwa_mov.R
import com.addincendekia.bwa_mov.utils.UserPreferences
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_signup_photo.*


class SignupPhotoActivity : AppCompatActivity(), PermissionListener {
    private val REQUEST_CODE_CAPTURE: Int = 1

    private var hasUploadPhoto = false

    private lateinit var userPref: UserPreferences
    private lateinit var fbStorageRef: StorageReference
    private lateinit var uploadedPhoto: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_photo)

        fbStorageRef = FirebaseStorage.getInstance().getReference()
        userPref = UserPreferences(this)

        if(!userPref.getValue("is_login").equals("1")) {
            startActivity(Intent(this@SignupPhotoActivity, SigninActivity::class.java))
        }

        tv_signup_username.text = userPref.getValue("username")
//        tv_signup_username.text = "Addin Cendekia"

        btn_signup_back.setOnClickListener{
            Toast.makeText(this, "tekan tombol upload nanti, untuk melewati proses upload photo", Toast.LENGTH_LONG).show()
        }
        btn_add_photo.setOnClickListener {
            if (!hasUploadPhoto) {
//                _toggleActionAddPhoto(true)
                Dexter.withActivity(this)
                    .withPermission(Manifest.permission.CAMERA)
                    .withListener(this)
                    .check()
            }else{
                _toggleActionAddPhoto( false)
            }
        }
        btn_signup_next.setOnClickListener {
//            val file = Uri.fromFile(File("path/to/images/rivers.jpg"))
//            val targetRef = fbStorageRef.child("images/rivers.jpg")
//
//            targetRef.putFile(file)
//                .addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
//                    // Get a URL to the uploaded content
////                    val downloadUrl = taskSnapshot.getDownloadUrl()
//                })
//                .addOnFailureListener(OnFailureListener {
//                    // Handle unsuccessful uploads
//                    // ...
//                })
        }
        btn_signup_skip.setOnClickListener {
            finishAffinity()
            startActivity(Intent(this@SignupPhotoActivity, MainActivity::class.java))
        }
    }
    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {
                takePictureIntent -> takePictureIntent.resolveActivity(packageManager).also {
                    startActivityForResult(takePictureIntent, REQUEST_CODE_CAPTURE)
                }
        }
    }

    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
        Toast.makeText(this, "anda tidak memiliki izin untuk mengakses camera", Toast.LENGTH_LONG).show()
    }
    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_CAPTURE && resultCode == Activity.RESULT_OK)
        {
            var bitmap = data?.extras?.get("data") as Bitmap

            uploadedPhoto = data.getData()!!

            Glide.with(this)
                .load(bitmap)
                .apply(RequestOptions.circleCropTransform())
                .into(iv_avatar)

            _toggleActionAddPhoto(true)
        }
    }
    private fun _toggleActionAddPhoto(isAddPhoto: Boolean) {
        hasUploadPhoto = isAddPhoto

        if (isAddPhoto) {
            btn_signup_next.visibility = View.VISIBLE
            btn_add_photo.setCompoundDrawablesWithIntrinsicBounds(
                null,
                ContextCompat.getDrawable(this, R.drawable.ic_close_white_24dp),
                null,
                null
            )
        }else{
            btn_signup_next.visibility = View.INVISIBLE
            btn_add_photo.setCompoundDrawablesWithIntrinsicBounds(
                null,
                ContextCompat.getDrawable(this, R.drawable.ic_add_white_24dp),
                null,
                null
            )
        }
    }
}
