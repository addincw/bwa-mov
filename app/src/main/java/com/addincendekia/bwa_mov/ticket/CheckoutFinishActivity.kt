package com.addincendekia.bwa_mov.ticket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.addincendekia.bwa_mov.R
import com.addincendekia.bwa_mov.main.MainActivity
import kotlinx.android.synthetic.main.activity_checkout_finish.*

class CheckoutFinishActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout_finish)

        btn_check_ticket.setOnClickListener {
            finishAffinity()
            startActivity(Intent(this, TicketActivity::class.java))
        }
        btn_go_home.setOnClickListener {
            finishAffinity()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}