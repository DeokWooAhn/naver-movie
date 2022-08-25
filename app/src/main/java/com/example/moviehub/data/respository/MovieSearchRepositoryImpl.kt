package com.example.moviehub.data.respository

import androidx.lifecycle.LiveData
import com.example.moviehub.data.api.RetrofitInstance.api
import com.example.moviehub.data.db.MovieSearchDatabase
import com.example.moviehub.data.model.Item
import com.example.moviehub.data.model.SearchResponse
import retrofit2.Response

class MovieSearchRepositoryImpl(
    private val db: MovieSearchDatabase
) : MovieSearchRepository {
    override suspend fun searchMovies(
        query: String
    ): Response<SearchResponse> {
        return api.searchMovies(query)
    }

    override suspend fun insertMovies(movie: Item) {
        db.movieSearchDao().insertMovie(movie)
    }

    override suspend fun deleteMovies(movie: Item) {
        db.movieSearchDao().deleteMovie(movie)
    }

    override fun getFavortieMovies(): LiveData<List<Item>> {
        return db.movieSearchDao().getFavoriteMovies()
    }
}