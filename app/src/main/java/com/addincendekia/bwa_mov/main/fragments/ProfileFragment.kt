package com.addincendekia.bwa_mov.main.fragments


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.addincendekia.bwa_mov.ProfileActivity

import com.addincendekia.bwa_mov.R
import com.addincendekia.bwa_mov.WalletActivity
import com.addincendekia.bwa_mov.auth.SigninActivity
import com.addincendekia.bwa_mov.utils.UserPreferences
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_movie.*
import kotlinx.android.synthetic.main.fragment_profile.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ProfileFragment : Fragment() {
    private lateinit var userPref: UserPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        userPref = UserPreferences(activity!!.applicationContext)

        tv_profile_name.text = userPref.getValue("nama")
        tv_profile_email.text = userPref.getValue("email")

        Glide.with(this)
            .load(userPref.getValue("url"))
            .apply(RequestOptions.circleCropTransform())
            .into(iv_profile_image)

        tv_profile_wallet.setOnClickListener {
            startActivity(Intent(activity?.applicationContext, WalletActivity::class.java))
        }
        tv_profile_edit.setOnClickListener {
            startActivity(Intent(activity?.applicationContext, ProfileActivity::class.java))
        }
        tv_profile_signout.setOnClickListener {
            ActivityCompat.finishAffinity(activity!!)

            userPref.reset()
            userPref.setValue("is_onboarding_done", "1")

            startActivity(Intent(activity?.applicationContext, SigninActivity::class.java))
        }
    }


}
