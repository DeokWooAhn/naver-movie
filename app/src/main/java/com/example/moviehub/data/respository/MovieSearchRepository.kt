package com.example.moviehub.data.respository

import androidx.lifecycle.LiveData
import com.example.moviehub.data.model.Item
import com.example.moviehub.data.model.SearchResponse
import retrofit2.Response

interface MovieSearchRepository {

    suspend fun searchMovies(
        query: String,
    ): Response<SearchResponse>

    // Room
    suspend fun insertMovies(movie: Item)

    suspend fun deleteMovies(movie: Item)

    fun getFavortieMovies(): LiveData<List<Item>>
}