package com.addincendekia.bwa_mov

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
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

class ProfileActivity : AppCompatActivity() {
    private lateinit var userPref: UserPreferences

    private var hasUploadPhoto = false

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
    }

    private fun _toggleActionAddPhoto(isAddPhoto: Boolean) {
        hasUploadPhoto = isAddPhoto

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
}