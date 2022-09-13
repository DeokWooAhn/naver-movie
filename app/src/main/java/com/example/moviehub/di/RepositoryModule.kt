package com.example.moviehub.di

import com.example.moviehub.data.respository.MovieSearchRepository
import com.example.moviehub.data.respository.MovieSearchRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindMovieSearchRepository(
        movieSearchRepositoryImpl: MovieSearchRepositoryImpl,
    ): MovieSearchRepository
}