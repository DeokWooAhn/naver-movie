package com.example.moviehub.data.respository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.moviehub.data.api.MovieSearchApi
import com.example.moviehub.data.db.MovieSearchDatabase
import com.example.moviehub.data.model.Item
import com.example.moviehub.data.model.SearchResponse
import com.example.moviehub.data.respository.MovieSearchRepositoryImpl.PerferencesKeys.CACHE_DELETE_MODE
import com.example.moviehub.util.Constants.PAGING_SIZE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieSearchRepositoryImpl @Inject constructor(
    private val db: MovieSearchDatabase,
    private val dataStore: DataStore<Preferences>,
    private val api: MovieSearchApi
) : MovieSearchRepository {
    override suspend fun searchMovies(
        query: String,
        display: Int
    ): Response<SearchResponse> {
        return api.searchMovies(query, display)
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

    // DataStore
    private object PerferencesKeys {
        val CACHE_DELETE_MODE = booleanPreferencesKey("cache_delete_mode")
    }

    override suspend fun saveCacheDeleteMode(mode: Boolean) {
        dataStore.edit { prefs ->
            prefs[CACHE_DELETE_MODE] = mode
        }
    }

    override suspend fun getCacheDeleteMode(): Flow<Boolean> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    exception.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { prefs ->
                prefs[CACHE_DELETE_MODE] ?: false
            }
    }

    //Paging
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

    override fun searchMoviesPaging(query: String, display: Int): Flow<PagingData<Item>> {
        val pagingSourceFactory = { MovieSearchPagingSource(api, query, display) }

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