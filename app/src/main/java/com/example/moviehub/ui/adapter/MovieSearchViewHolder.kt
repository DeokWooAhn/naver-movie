package com.example.moviehub.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.moviehub.data.model.Item
import com.example.moviehub.databinding.ItemMoviePreviewBinding

class MovieSearchViewHolder(
    private val binding: ItemMoviePreviewBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: Item) {
        var title = movie.title.replace("<b>", "")
        title = title.replace("</b>", "")

        itemView.apply {
            binding.ivArticleImage.load(movie.image)
            binding.tvTitle.text = title
            binding.tvDirector.text = movie.director
            binding.tvActor.text = movie.actor
        }
    }
}