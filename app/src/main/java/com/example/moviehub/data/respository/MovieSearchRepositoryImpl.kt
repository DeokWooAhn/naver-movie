package com.example.moviehub.data.respository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.moviehub.data.api.RetrofitInstance.api
import com.example.moviehub.data.db.MovieSearchDatabase
import com.example.moviehub.data.model.Item
import com.example.moviehub.data.model.SearchResponse
import com.example.moviehub.util.Constants.PAGING_SIZE
import kotlinx.coroutines.flow.Flow
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

    override fun getFavortieMovies(): Flow<List<Item>> {
        return db.movieSearchDao().getFavoriteMovies()
    }

    override fun getFavoritePagingMovies(): Flow<PagingData<Item>> {
        val pagingSourceFactory = { db.movieSearchDao().getFavoritePagingMovies() }

        return Pager(
            config = PagingConfig(
                pageSize = PAGING_SIZE,
                enablePlaceholders = false,
                maxSize = PAGING_SIZE * 3
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
}