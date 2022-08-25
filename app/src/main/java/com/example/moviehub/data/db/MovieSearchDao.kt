package com.example.moviehub.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.moviehub.data.model.Item

@Dao
interface MovieSearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: Item)

    @Delete
    suspend fun deleteMovie(movie: Item)

    @Query("SELECT * FROM movies")
    fun getFavoriteMovies(): LiveData<List<Item>>
}