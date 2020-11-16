package com.addincendekia.bwa_mov

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.addincendekia.bwa_mov.auth.REQUEST_CODE_CAPTURE
import com.addincendekia.bwa_mov.utils.UserPreferences
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.btn_add_photo
import kotlinx.android.synthetic.main.activity_profile.field_email
import kotlinx.android.synthetic.main.activity_profile.field_nama
import kotlinx.android.synthetic.main.activity_profile.field_username
import kotlinx.android.synthetic.main.activity_profile.iv_avatar
import kotlinx.android.synthetic.main.activity_signup_photo.*
import kotlinx.android.synthetic.main.fragment_movie.*
import java.io.File

class ProfileActivity : AppCompatActivity() {
    private lateinit var userPref: UserPreferences
    private lateinit var fileProvider: Uri

    private var hasTakePhoto = false
    private var takenPhoto: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

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
    private fun _getPhotoFile(fileName: String): File{
        val picturesDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".png", picturesDirectory)
    }
}