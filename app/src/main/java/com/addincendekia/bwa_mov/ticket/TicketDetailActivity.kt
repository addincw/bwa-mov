package com.addincendekia.bwa_mov.ticket

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.addincendekia.bwa_mov.R
import com.addincendekia.bwa_mov.adapters.CheckoutSeatAdapter
import com.addincendekia.bwa_mov.models.Film
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_checkout_preview.*
import kotlinx.android.synthetic.main.activity_movie.*
import kotlinx.android.synthetic.main.activity_ticket_detail.*
import kotlinx.android.synthetic.main.activity_ticket_detail.rv_preview_seats
import kotlinx.android.synthetic.main.modal_ticket_qrcode.*
import kotlinx.android.synthetic.main.modal_ticket_qrcode.view.*
import java.text.SimpleDateFormat
import java.util.*

class TicketDetailActivity : AppCompatActivity() {
    private lateinit var film: Film

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_detail)

        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val currentDate = dateFormat.format(Date())
        val selectedSeats = mutableSetOf<String>("A1", "A2", "A3", "A4")
        val adapter = CheckoutSeatAdapter(selectedSeats)

        film = intent.getParcelableExtra<Film>("film")

        tv_ticket_title.text = film.judul
        tv_ticket_genre.text = film.genre
        tv_ticket_rating.text = film.rating
        tv_ticket_date.text = currentDate
        tv_ticket_location.text = "Tunjungan Plaza, Cinema 1"
        tv_seat_label.text = "Seats (${adapter.itemCount})"

        Glide.with(this)
            .load(film.poster)
            .into(iv_ticket_poster)

        rv_preview_seats.layoutManager = LinearLayoutManager(this)
        rv_preview_seats.adapter = adapter

        btn_ticket_detail_back.setOnClickListener {
            finish()
        }
        iv_ticket_qrcode.setOnClickListener {
            val qrcodeView = LayoutInflater.from(this).inflate(R.layout.modal_ticket_qrcode, qrcode_container)
            val qrcodeModal = AlertDialog.Builder(this)
                .setView(qrcodeView)
                .setCancelable(false)
                .create()

            qrcodeView.btn_qrcode_close.setOnClickListener {
                qrcodeModal.dismiss()
            }

            if(qrcodeModal.window != null) {
                qrcodeModal.window!!.setBackgroundDrawable(ColorDrawable(0))
            }

            qrcodeModal.show()
        }
    }
}