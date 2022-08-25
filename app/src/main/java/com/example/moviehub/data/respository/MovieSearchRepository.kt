package com.example.moviehub.data.respository

import com.example.moviehub.data.model.SearchResponse
import retrofit2.Response

interface MovieSearchRepository {

    suspend fun searchMovies(
        query: String,
    ): Response<SearchResponse>
}