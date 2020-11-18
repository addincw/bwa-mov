package com.addincendekia.bwa_mov.ticket

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.addincendekia.bwa_mov.R
import com.addincendekia.bwa_mov.adapters.CheckoutSeatAdapter
import com.addincendekia.bwa_mov.main.MainActivity
import com.addincendekia.bwa_mov.utils.Currency
import com.addincendekia.bwa_mov.utils.UserPreferences
import kotlinx.android.synthetic.main.activity_checkout_preview.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

const val NOTIFICATION_CHANNEL_ID: String = "bwaMOV"
const val NOTIFICATION_CHANNEL_NAME: String = "BWA MOV"
const val NOTIFICATION_ID: Int = 0

class CheckoutPreviewActivity : AppCompatActivity() {
    private lateinit var userPref: UserPreferences
    private lateinit var notificationManager: NotificationManagerCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout_preview)

        createNotificationChannel()

        userPref = UserPreferences(this)
        notificationManager = createNotificationhManager()

        val dateFormat = SimpleDateFormat("dd M, yyyy")
        val currentDate = dateFormat.format(Date())
        val adapter = CheckoutSeatAdapter(userPref.getValue("checkout_film_seats", "stringSet") as MutableSet<String>)
        val totalCheckout = adapter.itemCount * 50000

        tv_preview_time_date.text = currentDate
        tv_preview_time_location.text = "Tunjungan Plaza, Cinema 1"
        tv_preview_total.text = "IDR " + Currency(totalCheckout.toDouble()).toRupiah()
        tv_saldo_balance.text = "IDR " + Currency(userPref.getValue("saldo")?.toDouble()).toRupiah()
        if(userPref.getValue("saldo")!!.toInt() < totalCheckout){
            tv_saldo_balance.setTextColor(ContextCompat.getColor(this, R.color.colorAppPrimary))
            tv_saldo_less_balance.visibility = TextView.VISIBLE
            btn_preview_buy.isEnabled = false
        }

        rv_preview_seats.layoutManager = LinearLayoutManager(this)
        rv_preview_seats.adapter = adapter

        bindViewListener()
    }

    private fun createNotificationhManager(): NotificationManagerCompat {
        return NotificationManagerCompat.from(this)
    }

    private fun bindViewListener() {
        btn_preview_back.setOnClickListener {
            finish()
        }
        btn_preview_cancel.setOnClickListener {
            userPref.removeValue("checkout_film")
            userPref.removeValue("checkout_film_seats")

            finishAffinity()
            startActivity(Intent(this, MainActivity::class.java))
        }
        btn_preview_buy.setOnClickListener {
            notificationManager.notify(NOTIFICATION_ID, notificationCheckoutFinish())

            userPref.removeValue("checkout_film")
            userPref.removeValue("checkout_film_seats")

            finishAffinity()
            startActivity(Intent(this, CheckoutFinishActivity::class.java))
        }
    }

    private fun notificationCheckoutFinish(): Notification {
        val intent = Intent(this, TicketDetailActivity::class.java)
            .putExtra("film_id", userPref.removeValue("checkout_film").toString())
        val pendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_movie_info_24dp)
            .setContentTitle("Pemesanan Ticket Berhasil!")
            .setContentText("Tiket berhasil dipesan, selamat menikmati filmnya ya")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .build()
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
                .apply {
                    lightColor = Color.WHITE
                    enableLights(true)
                }

            manager.createNotificationChannel(channel)
        }
    }
}