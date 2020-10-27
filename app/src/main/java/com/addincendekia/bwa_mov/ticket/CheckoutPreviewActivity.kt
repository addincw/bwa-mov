package com.addincendekia.bwa_mov.ticket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.addincendekia.bwa_mov.R
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.activity_checkout_preview.*

class CheckoutPreviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout_preview)

        tv_preview_time_date.text = "12 Okt, 2020"
        tv_preview_time_location.text = "Tunjungan Plaza, Cinema 1"
        tv_saldo_balance.text = "IDR 500.000"
    }
}