package com.addincendekia.bwa_mov

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.addincendekia.bwa_mov.utils.Currency
import com.addincendekia.bwa_mov.utils.UserPreferences
import kotlinx.android.synthetic.main.activity_wallet.*
import java.text.NumberFormat

class WalletActivity : AppCompatActivity() {
    private lateinit var userPref: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet)

        userPref = UserPreferences(this)

        tv_wallet_amount.text = "IDR " + Currency(userPref.getValue("saldo")?.toDouble()).toRupiah()

        btn_wallet_back.setOnClickListener {
            finish()
        }
    }
}