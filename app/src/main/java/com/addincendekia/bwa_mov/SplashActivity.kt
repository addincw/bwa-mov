package com.addincendekia.bwa_mov

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.addincendekia.bwa_mov.auth.SigninActivity
import com.addincendekia.bwa_mov.auth.SignupPhotoActivity
import com.addincendekia.bwa_mov.onboard.OnboardPlayingActivity
import com.addincendekia.bwa_mov.ticket.CheckoutActivity
import com.addincendekia.bwa_mov.utils.UserPreferences

class SplashActivity : AppCompatActivity() {
    lateinit var userPref: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        var handler = Handler()

        userPref = UserPreferences(this)
//        userPref.reset()

        handler.postDelayed({
            if(userPref.getValue("is_onboarding_done").equals("1")) {
                startActivity(Intent(this@SplashActivity, SigninActivity::class.java))
            }else{
                startActivity(Intent(this@SplashActivity, OnboardPlayingActivity::class.java))
            }

//            startActivity(Intent(this@SplashActivity, CheckoutActivity::class.java))

            finishAffinity()
        }, 3000)
    }
}
