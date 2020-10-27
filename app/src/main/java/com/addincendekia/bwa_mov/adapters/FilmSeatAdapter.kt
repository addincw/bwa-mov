package com.addincendekia.bwa_mov.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.addincendekia.bwa_mov.R
import com.addincendekia.bwa_mov.models.FilmActor
import com.addincendekia.bwa_mov.models.FilmSeat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class FilmSeatAdapter(private val filmSeatData: List<FilmSeat>) : RecyclerView.Adapter<FilmSeatAdapter.SeatViewHolder>() {
    private var onItemClickCallback: OnItemClickCallback? = null

    interface OnItemClickCallback {
        fun onItemClicked(filmSeat: FilmSeat)
    }

    class SeatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivSeat: ImageView = itemView.findViewById(R.id.iv_seat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeatViewHolder {
        var view: View = LayoutInflater.from(parent.context).inflate(R.layout.recycleview_filmseat, parent, false)
        return SeatViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filmSeatData.size
    }

    override fun onBindViewHolder(holder: FilmSeatAdapter.SeatViewHolder, position: Int) {
        val filmSeat = filmSeatData[position]
        var seatSate = R.drawable.movie_seat_empty

        if(filmSeat.status!!.equals(1)) seatSate = R.drawable.movie_seat_booked

        holder.ivSeat.setImageResource(seatSate)

        holder.itemView.setOnClickListener { onItemClickCallback?.onItemClicked(filmSeat) }
    }

    fun setOnItemClickCallback(onItemClickCallback: FilmSeatAdapter.OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

}
