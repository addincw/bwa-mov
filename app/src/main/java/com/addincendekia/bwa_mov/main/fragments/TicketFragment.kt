package com.addincendekia.bwa_mov.main.fragments


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.addincendekia.bwa_mov.MovieActivity

import com.addincendekia.bwa_mov.R
import com.addincendekia.bwa_mov.adapters.FilmAdapter
import com.addincendekia.bwa_mov.models.Film
import com.addincendekia.bwa_mov.ticket.TicketDetailActivity
import com.addincendekia.bwa_mov.ticket.TicketHistoryActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_ticket.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class TicketFragment : Fragment() {
    private lateinit var fbDB: FirebaseDatabase
    private lateinit var fbDBFilmRef: DatabaseReference

    private var films = ArrayList<Film>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ticket, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fbDB = FirebaseDatabase.getInstance()
        fbDBFilmRef = fbDB.getReference("Film")


        iv_movie_history.setOnClickListener{
            startActivity(Intent(activity?.applicationContext, TicketHistoryActivity::class.java))
        }

        rv_movies_perday.layoutManager = LinearLayoutManager(context)
        _fetchFilmSchedule(rv_movies_perday)
    }

    private fun _fetchFilmSchedule(rvTarget: RecyclerView) {
        fbDBFilmRef.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(dbError: DatabaseError) {
                Toast.makeText(context, dbError.message, Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(data: DataSnapshot) {
                films.clear()

                for(newData in data.children) {
                    var film = newData.getValue(Film::class.java)
                    films.add(film!!)
                }

                val decorator = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
                decorator.setDrawable(ContextCompat.getDrawable(activity!!.applicationContext, R.drawable.recycleview_devider)!!)

                val filmAdapter = FilmAdapter(films, R.layout.recycleview_film_list)
                filmAdapter.setOnItemClickCallback(object : FilmAdapter.OnItemClickCallback {
                    override fun onItemClicked(film: Film) {
                        startActivity(
                            Intent(context, TicketDetailActivity::class.java).putExtra(
                                "film",
                                film
                            )
                        )
                    }
                })

                tv_movies_perday.text = filmAdapter.itemCount.toString() + " Movies"

                rvTarget.adapter = filmAdapter
                rvTarget.addItemDecoration(decorator)
            }

        })
    }


}
