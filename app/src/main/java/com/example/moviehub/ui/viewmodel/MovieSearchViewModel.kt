package com.example.moviehub.ui.viewmodel

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.moviehub.data.model.Item
import com.example.moviehub.data.model.SearchResponse
import com.example.moviehub.data.respository.MovieSearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MovieSearchViewModel(
    private val movieSearchRepository: MovieSearchRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    // Api
    private val _searchResult = MutableLiveData<SearchResponse>()
    val searchResult: LiveData<SearchResponse> get() = _searchResult

    fun searchMovies(query: String) = viewModelScope.launch(Dispatchers.IO) {
        val response = movieSearchRepository.searchMovies(query)
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

    // Paging
    val favoritePagingMovies: StateFlow<PagingData<Item>> =
        movieSearchRepository.getFavoritePagingMovies()
            .cachedIn(viewModelScope)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PagingData.empty())

    companion object {
        private const val SAVED_STATE_KEY = "query"
    }
}