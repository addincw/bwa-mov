package com.addincendekia.bwa_mov.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.addincendekia.bwa_mov.R
import com.addincendekia.bwa_mov.models.Film
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.Glide








class FilmAdapter(private val filmData: ArrayList<Film>, private val layoutViewHoder: Int) : RecyclerView.Adapter<FilmAdapter.FilmViewHolder>() {
    private var onItemClickCallback: OnItemClickCallback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        var view: View = LayoutInflater.from(parent.context).inflate(layoutViewHoder, parent, false)
        return FilmViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filmData.size
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val film = filmData[position]

        Glide.with(holder.itemView.context)
            .load(film.poster)
            .into(holder.ivPoster)

        holder.tvJudul.text = film.judul
        holder.tvGenre.text = film.genre
        holder.tvRating.text = film.rating

        holder.itemView.setOnClickListener { onItemClickCallback?.onItemClicked(film) }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class FilmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvJudul: TextView = itemView.findViewById(R.id.tv_judul)
        var tvGenre: TextView = itemView.findViewById(R.id.tv_genre)
        var tvRating: TextView = itemView.findViewById(R.id.tv_rating)
        var ivPoster: ImageView = itemView.findViewById(R.id.iv_poster)
    }

    interface OnItemClickCallback {
        fun onItemClicked(film: Film)
    }
}
