package com.addincendekia.bwa_mov.ticket

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.addincendekia.bwa_mov.R
import com.addincendekia.bwa_mov.adapters.CheckoutSeatAdapter
import com.addincendekia.bwa_mov.main.MainActivity
import com.addincendekia.bwa_mov.utils.UserPreferences
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.activity_checkout_preview.*
import kotlinx.android.synthetic.main.fragment_movie.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class CheckoutPreviewActivity : AppCompatActivity() {
    private lateinit var userPref: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout_preview)

        userPref = UserPreferences(this)

        val dateFormat = SimpleDateFormat("dd M, yyyy")
        val currentDate = dateFormat.format(Date())
        val adapter = CheckoutSeatAdapter(userPref.getValue("checkout_film_seats", "stringSet") as MutableSet<String>)
        val totalCheckout = adapter.itemCount * 50000

        tv_preview_time_date.text = currentDate
        tv_preview_time_location.text = "Tunjungan Plaza, Cinema 1"
        tv_preview_total.text = "IDR " + _getCurrency(totalCheckout)
        tv_saldo_balance.text = "IDR " + _getCurrency(userPref.getValue("saldo")?.toInt())
        if(userPref.getValue("saldo")!!.toInt() < totalCheckout){
            tv_saldo_balance.setTextColor(ContextCompat.getColor(this, R.color.colorAppPrimary))
            tv_saldo_less_balance.visibility = TextView.VISIBLE
            btn_preview_buy.isEnabled = false
        }

        rv_preview_seats.layoutManager = LinearLayoutManager(this)
        rv_preview_seats.adapter = adapter

        btn_preview_back.setOnClickListener{
            finish()
        }
        btn_preview_cancel.setOnClickListener{
            userPref.removeValue("checkout_film")
            userPref.removeValue("checkout_film_seats")

            finishAffinity()
            startActivity(Intent(this, MainActivity::class.java))
        }
        btn_preview_buy.setOnClickListener{
            userPref.removeValue("checkout_film")
            userPref.removeValue("checkout_film_seats")

            finishAffinity()
            startActivity(Intent(this, CheckoutFinishActivity::class.java))
        }
    }

    private fun _getCurrency(currency: Int?): String {
        return NumberFormat.getInstance().format(currency)
    }
}