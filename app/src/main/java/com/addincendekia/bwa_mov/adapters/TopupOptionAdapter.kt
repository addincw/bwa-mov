package com.addincendekia.bwa_mov.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.addincendekia.bwa_mov.R
import com.addincendekia.bwa_mov.models.TopupOption
import com.addincendekia.bwa_mov.utils.Currency
import kotlinx.android.synthetic.main.recycleview_topupoption.view.*
import kotlinx.android.synthetic.main.recycleview_wallet_list.view.*

class TopupOptionAdapter(private val TopupOptionData: MutableList<TopupOption>) : RecyclerView.Adapter<TopupOptionAdapter.TopupOptionViewHolder>() {
    private var onItemClickCallback: OnItemClickCallback? = null

    interface OnItemClickCallback {
        fun onItemClicked(topupOption: TopupOption, position: Int)
    }

    inner class TopupOptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopupOptionViewHolder {
        var view: View = LayoutInflater.from(parent.context).inflate(R.layout.recycleview_topupoption, parent, false)
        return TopupOptionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return TopupOptionData.size
    }

    override fun onBindViewHolder(holder: TopupOptionAdapter.TopupOptionViewHolder, position: Int) {
        val topupOption = TopupOptionData[position]

        when(topupOption.selected){
            true -> {
                holder.itemView.tv_amount.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.colorAppPrimary))
                holder.itemView.tv_currency.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.colorAppPrimary))
                holder.itemView.iv_topup_option.setImageResource(R.drawable.topup_option_active)
            }
            else -> {
                holder.itemView.tv_amount.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
                holder.itemView.tv_currency.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.colorAppAccent))
                holder.itemView.iv_topup_option.setImageResource(R.drawable.topup_option)
            }
        }
        holder.itemView.tv_amount.text = Currency(topupOption.option!!.toDouble()).toRupiah()
        holder.itemView.setOnClickListener { onItemClickCallback?.onItemClicked(topupOption, position) }
    }

    fun setOnItemClickCallback(onItemClickCallback: TopupOptionAdapter.OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

}
