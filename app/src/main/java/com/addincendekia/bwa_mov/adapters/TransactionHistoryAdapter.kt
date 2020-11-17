package com.addincendekia.bwa_mov.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.addincendekia.bwa_mov.R
import com.addincendekia.bwa_mov.models.FilmActor
import com.addincendekia.bwa_mov.models.FilmSeat
import com.addincendekia.bwa_mov.models.TransactionHistory
import com.addincendekia.bwa_mov.utils.Currency
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_checkout_preview.*
import kotlinx.android.synthetic.main.recycleview_filmseat.view.*
import kotlinx.android.synthetic.main.recycleview_filmseat_checkout.view.*
import kotlinx.android.synthetic.main.recycleview_wallet_list.view.*

class TransactionHistoryAdapter(private val TransactionHistoryData: Set<TransactionHistory>) : RecyclerView.Adapter<TransactionHistoryAdapter.HistoryViewHolder>() {
    private var onItemClickCallback: OnItemClickCallback? = null

    interface OnItemClickCallback {
        fun onItemClicked(history: TransactionHistory, position: Int)
    }

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        var view: View = LayoutInflater.from(parent.context).inflate(R.layout.recycleview_wallet_list, parent, false)
        return HistoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return TransactionHistoryData.size
    }

    override fun onBindViewHolder(holder: TransactionHistoryAdapter.HistoryViewHolder, position: Int) {
        val history = TransactionHistoryData.elementAt(position)
        var amountFlag = when(history.type) {
            0 -> {
                holder.itemView.tv_rating.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.colorAppSuccess))
                "+"
            }
            1 -> {
                holder.itemView.tv_rating.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.colorAppPrimary))
                "-"
            }
            else -> {
                holder.itemView.tv_rating.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.colorAppSuccess))
                "+"
            }
        }

        holder.itemView.tv_judul.text = history.title
        holder.itemView.tv_genre.text = history.subtitle
        holder.itemView.tv_rating.text = amountFlag + " IDR ${Currency(history.amount!!.toDouble()).toRupiah()}"
        holder.itemView.setOnClickListener { onItemClickCallback?.onItemClicked(history, position) }
    }

    fun setOnItemClickCallback(onItemClickCallback: TransactionHistoryAdapter.OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

}
