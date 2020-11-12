package com.addincendekia.bwa_mov.main.fragments


import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.addincendekia.bwa_mov.R
import com.addincendekia.bwa_mov.adapters.FilmAdapter
import com.addincendekia.bwa_mov.models.Film
import com.addincendekia.bwa_mov.utils.UserPreferences
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_movie.*

import java.text.NumberFormat
import com.addincendekia.bwa_mov.MovieActivity
import com.addincendekia.bwa_mov.ProfileActivity


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class MovieFragment : Fragment() {
    private lateinit var fbDB: FirebaseDatabase
    private lateinit var fbDBFilmRef: DatabaseReference
    private lateinit var userPref: UserPreferences

    private var films = ArrayList<Film>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fbDB = FirebaseDatabase.getInstance()
        fbDBFilmRef = fbDB.getReference("Film")

        userPref = UserPreferences(activity!!.applicationContext)
//        userPref.reset()

        tv_username.text = userPref.getValue("username")
        tv_saldo.text = "IDR " + _getCurrency(userPref.getValue("saldo")?.toDouble())

        iv_url.setOnClickListener{
            startActivity(Intent(activity?.applicationContext, ProfileActivity::class.java))
        }
        Glide.with(this)
            .load(userPref.getValue("url"))
            .apply(RequestOptions.circleCropTransform())
            .into(iv_url)

        rv_now_playing.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rv_coming_soon.layoutManager = LinearLayoutManager(context)

        _fetchFilmNowPlaying(rv_now_playing)
        _fetchFilmComingSoon(rv_coming_soon)
    }

    private fun _fetchFilmNowPlaying(rvTarget: RecyclerView) {
        fbDBFilmRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(dbError: DatabaseError) {
                Toast.makeText(context, dbError.message, Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(data: DataSnapshot) {
                films.clear()

                for (newData in data.children) {
                    var film = newData.getValue(Film::class.java)
                    films.add(film!!)
                }

                val decorator = DividerItemDecoration(context, LinearLayoutManager.HORIZONTAL)
                decorator.setDrawable(
                    ContextCompat.getDrawable(
                        activity!!.applicationContext,
                        R.drawable.recycleview_devider
                    )!!
                )

                val filmAdapter = FilmAdapter(films, R.layout.recycleview_film_card)
                filmAdapter.setOnItemClickCallback(object : FilmAdapter.OnItemClickCallback {
                    override fun onItemClicked(film: Film) {
                        startActivity(
                            Intent(context, MovieActivity::class.java).putExtra(
                                "film",
                                film
                            )
                        )
                    }
                })

                rvTarget.adapter = filmAdapter
                rvTarget.addItemDecoration(decorator)
            }

        })
    }
    private fun _fetchFilmComingSoon(rvTarget: RecyclerView) {
        fbDBFilmRef.addValueEventListener(object: ValueEventListener{
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
                        startActivity(Intent(context, MovieActivity::class.java).putExtra("film", film))
                    }
                })

                rvTarget.adapter = filmAdapter
                rvTarget.addItemDecoration(decorator)
            }

        })
    }

    private fun _getCurrency(currency: Double?): String {
        return NumberFormat.getInstance().format(currency)
    }
}
