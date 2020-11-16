package com.addincendekia.bwa_mov.auth

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.addincendekia.bwa_mov.main.MainActivity
import com.addincendekia.bwa_mov.R
import com.addincendekia.bwa_mov.utils.UserPreferences
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.addincendekia.bwa_mov.models.User
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_signup_photo.*
import java.io.File

const val REQUEST_CODE_CAPTURE: Int = 1

class SignupPhotoActivity : AppCompatActivity() {
    private var hasTakePhoto = false
    private var takenPhoto: File? = null

    private lateinit var userPref: UserPreferences
    private lateinit var fbStorageRef: StorageReference
    private lateinit var fileProvider: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_photo)

        fbStorageRef = FirebaseStorage.getInstance().getReference()
        userPref = UserPreferences(this)

        if(!userPref.getValue("is_login").equals("1")) {
            startActivity(Intent(this@SignupPhotoActivity, SigninActivity::class.java))
        }

        tv_signup_username.text = userPref.getValue("username")

        btn_signup_back.setOnClickListener{
            Toast.makeText(this, "tekan tombol upload nanti, untuk melewati proses upload photo", Toast.LENGTH_LONG).show()
        }
        btn_add_photo.setOnClickListener {
            if (!hasTakePhoto) {
                takenPhoto = _getPhotoFile(userPref.getValue("username").toString())
                fileProvider = FileProvider.getUriForFile(this, "com.addincendekia.bwa_mov.fileprovider", takenPhoto!!)

                val takePhoto = Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

                if(takePhoto.resolveActivity(this.packageManager) != null){
                    startActivityForResult(takePhoto, REQUEST_CODE_CAPTURE)
                }else{
                    Toast.makeText(this, "tidak dapat membuka kamera", Toast.LENGTH_LONG).show()
                }
            }else{
                takenPhoto = null
                fileProvider = Uri.EMPTY

                iv_avatar.setImageResource(R.drawable.avatar_noimage)
                _toggleActionAddPhoto( false)
            }
        }
        btn_signup_next.setOnClickListener {
            val targetRef = fbStorageRef.child("images/${takenPhoto!!.name}")

            btn_signup_next.text = "loading..."
            btn_signup_next.isEnabled = false

            targetRef.putFile(fileProvider)
                .addOnSuccessListener{
                    // Get a URL to the uploaded content
                    _updateUserUrl(it.storage.downloadUrl.toString())
                }
                .addOnFailureListener{
                    Toast.makeText(this, it.message.toString() + ": " + takenPhoto!!.name + " in: " + takenPhoto!!.absolutePath, Toast.LENGTH_LONG).show()
                }
        }
        btn_signup_skip.setOnClickListener {
            finishAffinity()
            startActivity(Intent(this@SignupPhotoActivity, MainActivity::class.java))
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_CODE_CAPTURE && resultCode == Activity.RESULT_OK){
            Glide.with(this)
                .load(takenPhoto!!.absolutePath)
                .apply(RequestOptions.circleCropTransform())
                .into(iv_avatar)

            return _toggleActionAddPhoto(true)
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
    private fun _updateUserUrl(url: String) {
        var fbDB = FirebaseDatabase.getInstance()
        var fbDBUserRef = fbDB.getReference("User")

        var newUser = User()
        newUser.username = userPref.getValue("username")
        newUser.nama = userPref.getValue("nama")
        newUser.email = userPref.getValue("email")
        newUser.saldo = userPref.getValue("saldo")
        newUser.url = url

        fbDBUserRef.child(newUser.username.toString()).setValue(newUser)

        btn_signup_next.apply {
            setText(R.string.signup_action_next)
            isEnabled = true
        }

        Toast.makeText(this@SignupPhotoActivity, "upload foto success, selamat datang di bwa mov", Toast.LENGTH_LONG).show()

        userPref.setValue("url", url)

        finishAffinity()
        startActivity(Intent(this@SignupPhotoActivity, MainActivity::class.java))
    }
    private fun _toggleActionAddPhoto(isAddPhoto: Boolean) {
        hasTakePhoto = isAddPhoto

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
    private fun _getPhotoFile(fileName: String): File{
        val picturesDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".png", picturesDirectory)
    }
}
