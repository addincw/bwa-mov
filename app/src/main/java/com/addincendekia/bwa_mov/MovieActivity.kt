package com.addincendekia.bwa_mov

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.addincendekia.bwa_mov.adapters.FilmActorAdapter
import com.addincendekia.bwa_mov.adapters.FilmAdapter
import com.addincendekia.bwa_mov.models.Film
import com.addincendekia.bwa_mov.models.FilmActor
import com.addincendekia.bwa_mov.ticket.CheckoutActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_movie.*
import kotlinx.android.synthetic.main.fragment_movie.*

class MovieActivity : AppCompatActivity() {
    private lateinit var fbDB: FirebaseDatabase
    private lateinit var fbDBUserRef: DatabaseReference

    private var filmActors = ArrayList<FilmActor>()
    private lateinit var film: Film

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        film = intent.getParcelableExtra<Film>("film")

        fbDB = FirebaseDatabase.getInstance()
        fbDBUserRef = fbDB.getReference("Film")

        tv_movie_title.text = film.judul
        tv_movie_genre.text = film.genre
        tv_movie_rating.text = film.rating
        tv_movie_storyboard_detail.text = film.desc

        Glide.with(this)
            .load(film.poster)
            .into(iv_movie_poster)

        rv_movie_actor.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        _fetchFilmActor(rv_movie_actor)

        btn_movie_back.setOnClickListener{
            finish()
        }
        btn_selecting_seat.setOnClickListener{
            startActivity(Intent(this, CheckoutActivity::class.java).putExtra("film_title", film.judul))
        }
    }

    private fun _fetchFilmActor(rvTarget: RecyclerView) {
        fbDBUserRef.child(film.judul.toString()).child("play").addValueEventListener(object: ValueEventListener {
            override fun onCancelled(dbError: DatabaseError) {
                Toast.makeText(this@MovieActivity, dbError.message, Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(data: DataSnapshot) {
                filmActors.clear()

                for(newData in data.children) {
                    var filmActor = newData.getValue(FilmActor::class.java)
                    filmActors.add(filmActor!!)
                }

                val decorator = DividerItemDecoration(this@MovieActivity, LinearLayoutManager.VERTICAL)
                decorator.setDrawable(ContextCompat.getDrawable(this@MovieActivity, R.drawable.recycleview_devider)!!)

                rvTarget.adapter = FilmActorAdapter(filmActors)
                rvTarget.addItemDecoration(decorator)
            }

        })

    }
}