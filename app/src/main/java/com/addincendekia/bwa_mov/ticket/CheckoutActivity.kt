package com.addincendekia.bwa_mov.ticket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridLayout
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.addincendekia.bwa_mov.MovieActivity
import com.addincendekia.bwa_mov.R
import com.addincendekia.bwa_mov.adapters.FilmAdapter
import com.addincendekia.bwa_mov.adapters.FilmSeatAdapter
import com.addincendekia.bwa_mov.models.Film
import com.addincendekia.bwa_mov.models.FilmSeat
import com.addincendekia.bwa_mov.utils.UserPreferences
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.activity_signin.*
import kotlinx.android.synthetic.main.fragment_movie.*
import java.io.IOException
import kotlin.random.Random

class CheckoutActivity : AppCompatActivity() {
    var seatLeft: List<String> = listOf("A1", "A2", "B1", "B2", "C1", "C2", "D1", "D2")
    var seatRight: List<String> = listOf("A3", "A4", "B3", "B4", "C3", "C4", "D3", "D4")
    var seatSelected: MutableSet<String> = mutableSetOf()

    private lateinit var userPref: UserPreferences
    private lateinit var filmTitle: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        userPref = UserPreferences(this)

        filmTitle = userPref.getValue("checkout_film") ?: intent.getStringExtra("film_title")
        checkout_movie_title.text = filmTitle

        if(userPref.getValue("checkout_film_seats", "stringSet")!!.isNotEmpty()) {
            seatSelected.addAll(userPref.getValue("checkout_film_seats", "stringSet") as MutableSet<String>)
            _toggleActionBuy()
        }

        _fetchFilmSeat(rv_film_left_seat, seatLeft)
        _fetchFilmSeat(rv_film_right_seat, seatRight)

        btn_checkout_next.apply { isEnabled = false }
        btn_checkout_back.setOnClickListener{
            userPref.removeValue("checkout_film")
            userPref.removeValue("checkout_film_seats")
            finish()
        }
        btn_checkout_next.setOnClickListener{
            if(_storeSeatSelected()){
                startActivity(Intent(this, CheckoutPreviewActivity::class.java))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        userPref.removeValue("checkout_film")
        userPref.removeValue("checkout_film_seats")
    }

    private fun _storeSeatSelected(): Boolean {
        return try {
            userPref.setValue("checkout_film", filmTitle)
            userPref.setValue("checkout_film_seats", seatSelected)
            true
        }catch (err: IOException) {
            false
        }
    }

    private fun _fetchFilmSeat(rvTarget: RecyclerView, seatData: List<String>) {
        var filmSeat: MutableList<FilmSeat> = mutableListOf()

        for (seat in seatData) {
            var seatStatus = Random.nextInt(0, 2)
            if(seatSelected.contains(seat)) seatStatus = 2

            filmSeat.add(FilmSeat(seat, seatStatus))
        }

        rvTarget.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)

        var seatAdapter = FilmSeatAdapter(filmSeat)
        seatAdapter.setOnItemClickCallback(object : FilmSeatAdapter.OnItemClickCallback {
            override fun onItemClicked(filmSeat: FilmSeat, position: Int) {
                if (filmSeat.status == 1) {
                    Toast.makeText(this@CheckoutActivity, "Kursi telah di booked oleh pengguna lain", Toast.LENGTH_LONG)
                        .show()
                    return
                }

                _iterateSeatSelected(filmSeat)
                _toggleActionBuy()

                seatAdapter.notifyItemChanged(position)
            }
        })
        rvTarget.adapter = seatAdapter
    }

    private fun _toggleActionBuy() {
        var totalSeatSelected = ""
        if(seatSelected.size > 0) {
            totalSeatSelected = " (${seatSelected.size})"
            btn_checkout_next.apply { isEnabled = true }
        }else{
            btn_checkout_next.apply { isEnabled = false }
        }

        btn_checkout_next.text = resources.getString(R.string.checkout_action_buy) + totalSeatSelected
    }

    private fun _iterateSeatSelected(filmSeat: FilmSeat) {
        if (filmSeat.status == 2) {
            filmSeat.status = 0

            seatSelected.remove(filmSeat.seat.toString())
        }else{
            filmSeat.status = 2

            seatSelected.add(filmSeat.seat.toString())
        }
    }
}