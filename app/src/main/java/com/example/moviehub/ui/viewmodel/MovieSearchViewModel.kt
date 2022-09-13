package com.example.moviehub.ui.viewmodel

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.work.*
import com.example.moviehub.data.model.Item
import com.example.moviehub.data.model.SearchResponse
import com.example.moviehub.data.respository.MovieSearchRepository
import com.example.moviehub.worker.CacheDeleteWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MovieSearchViewModel @Inject constructor(
    private val movieSearchRepository: MovieSearchRepository,
    private val workManager: WorkManager,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    // Api
    private val _searchResult = MutableLiveData<SearchResponse>()
    val searchResult: LiveData<SearchResponse> get() = _searchResult

    fun searchMovies(query: String) = viewModelScope.launch(Dispatchers.IO) {
        val response = movieSearchRepository.searchMovies(query, 1)
        if (response.isSuccessful) {
            response.body()?.let { body ->
                _searchResult.postValue(body)
            }
        }
    }

    // Room
    fun saveMovie(movie: Item) = viewModelScope.launch(Dispatchers.IO) {
        movieSearchRepository.insertMovies(movie)
    }

    fun deleteMovie(movie: Item) = viewModelScope.launch(Dispatchers.IO) {
        movieSearchRepository.deleteMovies(movie)
    }

    //    val favoriteMovies: Flow<List<Item>> = movieSearchRepository.getFavortieMovies()
    val favoriteMovies: StateFlow<List<Item>> = movieSearchRepository.getFavortieMovies()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), listOf())

    // SavedState
    var query = String()
        set(value) {
            field = value
            savedStateHandle.set(SAVED_STATE_KEY, value)
        }

    init {
        query = savedStateHandle.get<String>(SAVED_STATE_KEY) ?: ""
    }

    // DataStore
    fun saveCacheDeleteMode(value: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        movieSearchRepository.saveCacheDeleteMode(value)
    }

    suspend fun getCacheDeleteMode() = withContext(Dispatchers.IO) {
        movieSearchRepository.getCacheDeleteMode().first()
    }

    // Paging
    val favoritePagingMovies: StateFlow<PagingData<Item>> =
        movieSearchRepository.getFavoritePagingMovies()
            .cachedIn(viewModelScope)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PagingData.empty())

    private val _searchPagingResult = MutableStateFlow<PagingData<Item>>(PagingData.empty())
    val searchPagingResult: StateFlow<PagingData<Item>> = _searchPagingResult.asStateFlow()

    fun searchMoviesPaging(query: String) {
        viewModelScope.launch {
            movieSearchRepository.searchMoviesPaging(query, display = 1)
                .cachedIn(viewModelScope)
                .collect {
                    _searchPagingResult.value = it
                }
        }
    }

    // WorkManager
    fun setWork() {
        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .setRequiresBatteryNotLow(true)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<CacheDeleteWorker>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            WORKER_KEY, ExistingPeriodicWorkPolicy.REPLACE, workRequest
        )
    }

    fun deleteWork() = workManager.cancelUniqueWork(WORKER_KEY)

    fun getWorkStatus(): LiveData<MutableList<WorkInfo>> =
        workManager.getWorkInfosForUniqueWorkLiveData(WORKER_KEY)

    companion object {
        private const val SAVED_STATE_KEY = "query"
        private const val WORKER_KEY = "cache_worker"
    }
}