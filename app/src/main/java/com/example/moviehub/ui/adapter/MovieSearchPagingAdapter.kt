package com.example.moviehub.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.moviehub.data.model.Item
import com.example.moviehub.databinding.ItemMoviePreviewBinding

class MovieSearchPagingAdapter : PagingDataAdapter<Item, MovieSearchViewHolder>(MovieDiffCallback) {
    override fun onBindViewHolder(holder: MovieSearchViewHolder, position: Int) {
        val pageMovie = getItem(position)
        pageMovie?.let { movie ->
            holder.bind(movie)
            holder.itemView.setOnClickListener {
                onItemClcikListener?.let { it(movie) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieSearchViewHolder {
        return MovieSearchViewHolder(
            ItemMoviePreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    private var onItemClcikListener: ((Item) -> Unit)? = null
    fun setOnItemClickListener(listener: (Item) -> Unit) {
        onItemClcikListener = listener
    }

    companion object {
        private val MovieDiffCallback = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.link == newItem.link
            }

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem == newItem
            }

        }
    }
}