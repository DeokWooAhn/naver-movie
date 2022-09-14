package com.example.moviehub.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.moviehub.data.model.Item
import com.example.moviehub.data.respository.MovieSearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val movieSearchRepository: MovieSearchRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {


    // Paging
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

    // SavedState
    var query = String()
        set(value) {
            field = value
            savedStateHandle.set(SAVED_STATE_KEY, value)
        }

    init {
        query = savedStateHandle.get<String>(SAVED_STATE_KEY) ?: ""
    }

    companion object {
        private const val SAVED_STATE_KEY = "query"
    }
}