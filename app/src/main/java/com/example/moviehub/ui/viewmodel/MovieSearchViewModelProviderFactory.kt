package com.example.moviehub.ui.viewmodel

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import androidx.work.WorkManager
import com.example.moviehub.data.respository.MovieSearchRepository

@Suppress("UNCHECKED_CAST")
//class MovieSearchViewModelProviderFactory(
//    private val movieSearchRepository: MovieSearchRepository
//) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(MovieSearchViewModel::class.java)) {
//            return MovieSearchViewModel(movieSearchRepository) as T
//        }
//        throw IllegalArgumentException("ViewModel class not found")
//    }
//}

class MovieSearchViewModelProviderFactory(
    private val movieSearchRepository: MovieSearchRepository,
    private val workManager: WorkManager,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null,
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        if (modelClass.isAssignableFrom(MovieSearchViewModel::class.java)) {
            return MovieSearchViewModel(movieSearchRepository, workManager, handle) as T
        }
        throw IllegalArgumentException("ViewModel class not found")
    }
}