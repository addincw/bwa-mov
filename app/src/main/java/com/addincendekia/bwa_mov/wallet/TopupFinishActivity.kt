package com.addincendekia.bwa_mov.wallet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.addincendekia.bwa_mov.R
import com.addincendekia.bwa_mov.main.MainActivity
import kotlinx.android.synthetic.main.activity_topup.*
import kotlinx.android.synthetic.main.activity_topup_finish.*

class TopupFinishActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topup_finish)

        btn_topup_search_film.setOnClickListener {
            Intent(this, MainActivity::class.java).also {
                finishAffinity()
                startActivity(it)
            }
        }
        btn_topup_my_wallet.setOnClickListener {
            Intent(this, WalletActivity::class.java).also {
                finish()
                startActivity(it)
            }
        }
    }
}