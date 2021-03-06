package com.nandra.movieverse.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nandra.movieverse.R
import com.nandra.movieverse.adapter.SearchRecyclerViewAdapter.SearchViewHolder
import com.nandra.movieverse.network.Film
import com.nandra.movieverse.ui.SearchFragmentDirections
import com.nandra.movieverse.util.Constant
import kotlinx.android.synthetic.main.item_search.view.*

class SearchRecyclerViewAdapter(
    private val searchResult: List<Film>,
    private val type: String
) : RecyclerView.Adapter<SearchViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
        return SearchViewHolder(view)
    }

    override fun getItemCount(): Int {
        return searchResult.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val currentFilm = searchResult[position]
        if(type == Constant.MOVIE_FILM_TYPE) {
            bindMovieViewProperties(holder, currentFilm)
        } else {
            bindTVViewProperties(holder, currentFilm)
        }
    }

    private fun bindMovieViewProperties(holder: SearchViewHolder, currentFilm: Film) {
        if (!currentFilm.posterPath.isNullOrEmpty()) {
            val url = "https://image.tmdb.org/t/p/w185"
            Glide.with(holder.itemView)
                .load(url + currentFilm.posterPath)
                .into(holder.itemView.item_search_poster)
        }
        holder.itemView.apply {
            val rating = "${currentFilm.voteAverage} / 10"
            item_search_title.text = currentFilm.title
            item_search_rating.text = rating
            item_search_released_date.text = currentFilm.releaseDate
        }
        holder.itemView.setOnClickListener {
            hideKeyboard(holder.itemView, holder.itemView.context)
            val action = SearchFragmentDirections.actionSearchFragmentToDetailFragment(type).setId(currentFilm.id.toString())
            holder.itemView.findNavController().navigate(action)
        }
    }

    private fun bindTVViewProperties(holder: SearchViewHolder, currentFilm: Film) {
        if (!currentFilm.posterPath.isNullOrEmpty()) {
            val url = "https://image.tmdb.org/t/p/w185"
            Glide.with(holder.itemView)
                .load(url + currentFilm.posterPath)
                .into(holder.itemView.item_search_poster)
        }
        holder.itemView.apply {
            val rating = "${currentFilm.voteAverage} / 10"
            item_search_title.text = currentFilm.tvName
            item_search_rating.text = rating
            item_search_released_date.text = currentFilm.tvAirDate
        }
        holder.itemView.setOnClickListener {
            hideKeyboard(holder.itemView, holder.itemView.context)
            val action = SearchFragmentDirections.actionSearchFragmentToDetailFragment(type).setId(currentFilm.id.toString())
            holder.itemView.findNavController().navigate(action)
        }
    }

    private fun hideKeyboard(view: View, context: Context) : Boolean {
        val inputMethodManager =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager!!.hideSoftInputFromWindow(view.windowToken, 0)
        return false
    }

    class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view)
}