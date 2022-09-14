package com.example.moviehub.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.moviehub.data.model.Item
import com.example.moviehub.data.respository.MovieSearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val movieSearchRepository: MovieSearchRepository,
) : ViewModel() {

    // Paging
    val favoritePagingMovies: StateFlow<PagingData<Item>> =
        movieSearchRepository.getFavoritePagingMovies()
            .cachedIn(viewModelScope)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PagingData.empty())

    // Room
    fun saveMovie(movie: Item) = viewModelScope.launch(Dispatchers.IO) {
        movieSearchRepository.insertMovies(movie)
    }

    fun deleteMovie(movie: Item) = viewModelScope.launch(Dispatchers.IO) {
        movieSearchRepository.deleteMovies(movie)
    }
}