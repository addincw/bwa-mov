package com.addincendekia.bwa_mov

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.addincendekia.bwa_mov.auth.REQUEST_CODE_CAPTURE
import com.addincendekia.bwa_mov.auth.SignupPhotoActivity
import com.addincendekia.bwa_mov.models.User
import com.addincendekia.bwa_mov.utils.UserPreferences
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.btn_add_photo
import kotlinx.android.synthetic.main.activity_profile.field_email
import kotlinx.android.synthetic.main.activity_profile.field_nama
import kotlinx.android.synthetic.main.activity_profile.field_username
import kotlinx.android.synthetic.main.activity_profile.iv_avatar
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.activity_signup_photo.*
import kotlinx.android.synthetic.main.fragment_movie.*
import java.io.File
import java.io.IOException

class ProfileActivity : AppCompatActivity() {
    private lateinit var fbStorageRef: StorageReference
    private lateinit var fbDB: FirebaseDatabase
    private lateinit var fbDBUserRef: DatabaseReference
    private lateinit var userPref: UserPreferences
    private lateinit var fileProvider: Uri

    private var hasTakePhoto = false
    private var takenPhoto: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        fbDB = FirebaseDatabase.getInstance()
        fbDBUserRef = fbDB.getReference("User")

        fbStorageRef = FirebaseStorage.getInstance().reference

        userPref = UserPreferences(this)

        field_username.setText(userPref.getValue("username"))
        field_nama.setText(userPref.getValue("nama"))
        field_email.setText(userPref.getValue("email"))

        if (!iv_avatar.equals("")){
            Glide.with(this)
                .load(userPref.getValue("url"))
                .apply(RequestOptions.circleCropTransform())
                .into(iv_avatar)

            _toggleActionAddPhoto(true)
        }

        btn_edit_back.setOnClickListener {
            finish()
        }
        btn_add_photo.setOnClickListener {
            if (!hasTakePhoto) {
                takenPhoto = _getPhotoFile(userPref.getValue("username").toString())
                fileProvider = FileProvider.getUriForFile(
                    this,
                    "com.addincendekia.bwa_mov.fileprovider",
                    takenPhoto!!
                )

                val takePhoto = Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(
                    MediaStore.EXTRA_OUTPUT,
                    fileProvider
                )

                if (takePhoto.resolveActivity(this.packageManager) != null) {
                    startActivityForResult(takePhoto, REQUEST_CODE_CAPTURE)
                } else {
                    Toast.makeText(this, "tidak dapat membuka kamera", Toast.LENGTH_LONG).show()
                }
            } else {
                takenPhoto = null
                fileProvider = Uri.EMPTY

                iv_avatar.setImageResource(R.drawable.avatar_noimage)
                _toggleActionAddPhoto(false)
            }
        }
        btn_edit_save.setOnClickListener {
            var user = _getCurrentUser()

            btn_edit_save.apply {
                text = "loading..."
                isEnabled = false
            }

            if(takenPhoto == null) {
                _updateUserData(user)
            }else{
                val targetRef = fbStorageRef.child("images/${takenPhoto!!.name}")

                targetRef.putFile(fileProvider)
                    .addOnSuccessListener {
                        targetRef.downloadUrl.addOnSuccessListener {
                            user.url = it.toString()
                            _updateUserData(user)
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "error upload photo: ${it.message}", Toast.LENGTH_LONG)
                            .show()
                    }
            }

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
    private fun _toggleActionAddPhoto(isAddPhoto: Boolean) {
        hasTakePhoto = isAddPhoto

        if (isAddPhoto) {
            btn_add_photo.setCompoundDrawablesWithIntrinsicBounds(
                null,
                ContextCompat.getDrawable(this, R.drawable.ic_close_white_24dp),
                null,
                null
            )
        }else{
            btn_add_photo.setCompoundDrawablesWithIntrinsicBounds(
                null,
                ContextCompat.getDrawable(this, R.drawable.ic_add_white_24dp),
                null,
                null
            )
        }
    }
    private fun _getCurrentUser(): User {
        val user = User()

        user.nama = field_nama.text.toString()
        user.email = field_email.text.toString()
        user.username = field_username.text.toString()
        user.password = userPref.getValue("password")
        user.url = userPref.getValue("url")
        user.saldo = userPref.getValue("saldo")

        return user
    }
    private fun _getPhotoFile(fileName: String): File{
        val picturesDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".png", picturesDirectory)
    }
    private fun _updateUserData(currentUser: User){
        fbDBUserRef.child(currentUser.username.toString())
            .setValue(currentUser)
            .addOnSuccessListener {
                Toast.makeText(this@ProfileActivity, "berhasil memperbarui data", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                btn_edit_save.setText(R.string.edit_save)
                Toast.makeText(this@ProfileActivity, "terjadi kesalahan dalam memperbarui data: ${it.message}", Toast.LENGTH_LONG).show()
            }
            .addOnCompleteListener {
                btn_edit_save.apply {
                    setText(R.string.edit_save)
                    isEnabled = true
                }
            }

        fbDBUserRef.child(currentUser.username.toString()).addValueEventListener(
            object: ValueEventListener{
                override fun onDataChange(data: DataSnapshot) {
                    if(_storeUserData(data.getValue(User::class.java))) {
                        Toast.makeText(this@ProfileActivity, "berhasil sinkronisasi data", Toast.LENGTH_LONG).show()
                    }else {
                        Toast.makeText(this@ProfileActivity, "terjadi kesalahan dalam sinkronisasi data", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onCancelled(err: DatabaseError) {
                    btn_edit_save.setText(R.string.edit_save)
                    Toast.makeText(this@ProfileActivity, err.message, Toast.LENGTH_LONG).show()
                }

            }
        )
    }
    private fun _storeUserData(user: User?): Boolean {
        return try {
            userPref.setValue("username", user?.username.toString())
            userPref.setValue("nama", user?.nama.toString())
            userPref.setValue("email", user?.email.toString())
            userPref.setValue("url", user?.url.toString())
            userPref.setValue("saldo", user?.saldo.toString())

            true
        }catch (e: IOException){
            false
        }
    }
}