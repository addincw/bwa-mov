package com.addincendekia.bwa_mov.ticket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridLayout
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.addincendekia.bwa_mov.MovieActivity
import com.addincendekia.bwa_mov.R
import com.addincendekia.bwa_mov.adapters.FilmAdapter
import com.addincendekia.bwa_mov.adapters.FilmSeatAdapter
import com.addincendekia.bwa_mov.models.Film
import com.addincendekia.bwa_mov.models.FilmSeat
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.fragment_movie.*
import kotlin.random.Random

class CheckoutActivity : AppCompatActivity() {
    var seatLeft: List<String> = listOf("A1", "A2", "B1", "B2", "C1", "C2", "D1", "D2")
    var seatRight: List<String> = listOf("A3", "A4", "B3", "B4", "C3", "C4", "D3", "D4")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        var filmSeatLeft: MutableList<FilmSeat> = mutableListOf()
        for (seat in seatLeft){
            filmSeatLeft.add(FilmSeat(seat, Random.nextInt(0, 2)))
        }
        rv_film_left_seat.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        var leftSeatAdapter = FilmSeatAdapter(filmSeatLeft)
        leftSeatAdapter.setOnItemClickCallback(object : FilmSeatAdapter.OnItemClickCallback {
            override fun onItemClicked(filmSeat: FilmSeat) {
                Toast.makeText(this@CheckoutActivity, filmSeat.seat + ": " + filmSeat.status, Toast.LENGTH_LONG).show()
            }
        })
        rv_film_left_seat.adapter = leftSeatAdapter

        var filmSeatRight: MutableList<FilmSeat> = mutableListOf()
        for (seat in seatRight){
            filmSeatRight.add(FilmSeat(seat, Random.nextInt(0, 2)))
        }
        rv_film_right_seat.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        var rightSeatAdapter = FilmSeatAdapter(filmSeatRight)
        rightSeatAdapter.setOnItemClickCallback(object : FilmSeatAdapter.OnItemClickCallback {
            override fun onItemClicked(filmSeat: FilmSeat) {
                Toast.makeText(this@CheckoutActivity, filmSeat.seat + ": " + filmSeat.status, Toast.LENGTH_LONG).show()
            }
        })
        rv_film_right_seat.adapter = rightSeatAdapter
    }
}