package com.nandra.movieverse.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nandra.movieverse.R
import com.nandra.movieverse.network.Cast
import kotlinx.android.synthetic.main.item_detail_cast.view.*

class CastRecyclerViewAdapter(private val castList: List<Cast>) : RecyclerView.Adapter<CastRecyclerViewAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_detail_cast, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return castList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentCast = castList[position]
        holder.itemView.item_detail_cast_name.text = currentCast.name
        holder.itemView.item_detail_cast_character.text = currentCast.character
        if (currentCast.profilePath != null){
            val url = "https://image.tmdb.org/t/p/w154"
            Glide.with(holder.itemView.context)
                .load(url + currentCast.profilePath)
                .into(holder.itemView.item_detail_cast_photo)
        } else {
            Glide.with(holder.itemView.context)
                .load(R.drawable.img_back_portrait_default)
                .into(holder.itemView.item_detail_cast_photo)
        }
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}