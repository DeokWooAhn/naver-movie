package com.example.moviehub.data.respository

import androidx.paging.PagingData
import com.example.moviehub.data.model.Item
import com.example.moviehub.data.model.SearchResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface MovieSearchRepository {

    suspend fun searchMovies(
        query: String,
        display: Int,
    ): Response<SearchResponse>

    // Room
    suspend fun insertMovies(movie: Item)

    suspend fun deleteMovies(movie: Item)

    fun getFavortieMovies(): Flow<List<Item>>

    suspend fun saveCacheDeleteMode(mode: Boolean)

    suspend fun getCacheDeleteMode(): Flow<Boolean>

    // Paging
    fun getFavoritePagingMovies(): Flow<PagingData<Item>>

    fun searchMoviesPaging(query: String, display: Int): Flow<PagingData<Item>>
}