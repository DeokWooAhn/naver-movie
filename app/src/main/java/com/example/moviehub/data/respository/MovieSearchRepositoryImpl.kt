package com.example.moviehub.data.respository

import com.example.moviehub.data.api.RetrofitInstance.api
import com.example.moviehub.data.model.SearchResponse
import retrofit2.Response

class MovieSearchRepositoryImpl : MovieSearchRepository {
    override suspend fun searchMovies(
        query: String
    ): Response<SearchResponse> {
        return api.searchMovies(query)
    }
}