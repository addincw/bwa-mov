package com.addincendekia.bwa_mov.ticket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.addincendekia.bwa_mov.R
import com.addincendekia.bwa_mov.models.Film
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_movie.*
import kotlinx.android.synthetic.main.activity_ticket_detail.*
import java.text.SimpleDateFormat
import java.util.*

class TicketDetailActivity : AppCompatActivity() {
    private lateinit var film: Film

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_detail)

        val dateFormat = SimpleDateFormat("dd m, yyyy")
        val currentDate = dateFormat.format(Date())

        film = intent.getParcelableExtra<Film>("film")

        tv_ticket_title.text = film.judul
        tv_ticket_genre.text = film.genre
        tv_ticket_rating.text = film.rating
        tv_ticket_date.text = currentDate
        tv_ticket_location.text = "Tunjungan Plaza, Cinema 1"

        Glide.with(this)
            .load(film.poster)
            .into(iv_ticket_poster)

        btn_ticket_detail_back.setOnClickListener {
            finish()
        }
    }
}