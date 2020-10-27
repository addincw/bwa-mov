package com.addincendekia.bwa_mov.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.addincendekia.bwa_mov.R
import com.addincendekia.bwa_mov.models.FilmActor
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class FilmActorAdapter(private val actorData: ArrayList<FilmActor>) : RecyclerView.Adapter<FilmActorAdapter.ActorViewHolder>() {
    private var onItemClickCallback: OnItemClickCallback? = null

    interface OnItemClickCallback {
        fun onItemClicked(filmActor: FilmActor)
    }

    class ActorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView = itemView.findViewById(R.id.tv_name)
        var tvUrl: ImageView = itemView.findViewById(R.id.iv_url)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder {
        var view: View = LayoutInflater.from(parent.context).inflate(R.layout.recycleview_filmactor, parent, false)
        return ActorViewHolder(view)
    }

    override fun getItemCount(): Int {
        return actorData.size
    }

    override fun onBindViewHolder(holder: FilmActorAdapter.ActorViewHolder, position: Int) {
        val filmActor = actorData[position]

        Glide.with(holder.itemView.context)
            .load(filmActor.url)
            .apply(RequestOptions.circleCropTransform())
            .into(holder.tvUrl)

        holder.tvName.text = filmActor.nama

        holder.itemView.setOnClickListener { onItemClickCallback?.onItemClicked(filmActor) }
    }

    fun setOnItemClickCallback(onItemClickCallback: FilmActorAdapter.OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

}
