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
import kotlinx.android.synthetic.main.recycleview_filmseat.view.*
import kotlinx.android.synthetic.main.recycleview_filmseat_checkout.view.*

class CheckoutSeatAdapter(private val checkoutSeatData: Set<String>) : RecyclerView.Adapter<CheckoutSeatAdapter.SeatViewHolder>() {
    private var onItemClickCallback: OnItemClickCallback? = null

    interface OnItemClickCallback {
        fun onItemClicked(checkoutSeat: String, position: Int)
    }

    inner class SeatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeatViewHolder {
        var view: View = LayoutInflater.from(parent.context).inflate(R.layout.recycleview_filmseat_checkout, parent, false)
        return SeatViewHolder(view)
    }

    override fun getItemCount(): Int {
        return checkoutSeatData.size
    }

    override fun onBindViewHolder(holder: CheckoutSeatAdapter.SeatViewHolder, position: Int) {
        val checkoutSeat = checkoutSeatData.elementAt(position)

        holder.itemView.tv_seat_number.text = "Seat " + checkoutSeat
        holder.itemView.setOnClickListener { onItemClickCallback?.onItemClicked(checkoutSeat, position) }
    }

    fun setOnItemClickCallback(onItemClickCallback: CheckoutSeatAdapter.OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

}
