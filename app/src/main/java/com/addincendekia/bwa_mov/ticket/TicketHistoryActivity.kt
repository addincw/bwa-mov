package com.addincendekia.bwa_mov.ticket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.addincendekia.bwa_mov.R
import com.addincendekia.bwa_mov.adapters.FilmAdapter
import com.addincendekia.bwa_mov.models.Film
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_ticket_history.*

class TicketHistoryActivity : AppCompatActivity() {
    private lateinit var fbDB: FirebaseDatabase
    private lateinit var fbDBFilmRef: DatabaseReference

    private var films = ArrayList<Film>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_history)

        fbDB = FirebaseDatabase.getInstance()
        fbDBFilmRef = fbDB.getReference("Film")

        btn_ticket_history_back.setOnClickListener{
            finish()
        }

        rv_movies_history.layoutManager = LinearLayoutManager(this)
        _fetchFilmHistory(rv_movies_history)
    }

    private fun _fetchFilmHistory(rvTarget: RecyclerView) {
        fbDBFilmRef.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(dbError: DatabaseError) {
                Toast.makeText(this@TicketHistoryActivity, dbError.message, Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(data: DataSnapshot) {
                films.clear()

                for(newData in data.children) {
                    var film = newData.getValue(Film::class.java)
                    films.add(film!!)
                }

                val decorator = DividerItemDecoration(this@TicketHistoryActivity, LinearLayoutManager.VERTICAL)
                decorator.setDrawable(ContextCompat.getDrawable(this@TicketHistoryActivity, R.drawable.recycleview_devider)!!)

                val filmAdapter = FilmAdapter(films, R.layout.recycleview_film_list)
                filmAdapter.setOnItemClickCallback(object : FilmAdapter.OnItemClickCallback {
                    override fun onItemClicked(film: Film) {
                        startActivity(
                            Intent(this@TicketHistoryActivity, TicketDetailActivity::class.java).putExtra(
                                "film",
                                film
                            )
                        )
                    }
                })

                tv_ticket_history_total.text = filmAdapter.itemCount.toString() + " Movies"

                rvTarget.adapter = filmAdapter
                rvTarget.addItemDecoration(decorator)
            }

        })
    }
}