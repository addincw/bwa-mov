package com.addincendekia.bwa_mov

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.addincendekia.bwa_mov.utils.UserPreferences
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.field_email
import kotlinx.android.synthetic.main.activity_profile.field_nama
import kotlinx.android.synthetic.main.activity_profile.field_username
import kotlinx.android.synthetic.main.fragment_movie.*

class ProfileActivity : AppCompatActivity() {
    private lateinit var userPref: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        userPref = UserPreferences(this)

        field_username.setText(userPref.getValue("username"))
        field_password.setText(userPref.getValue("password"))
        field_nama.setText(userPref.getValue("nama"))
        field_email.setText(userPref.getValue("email"))

        Glide.with(this)
            .load(userPref.getValue("url"))
            .apply(RequestOptions.circleCropTransform())
            .into(iv_avatar)

        btn_edit_back.setOnClickListener {
            finish()
        }
    }
}