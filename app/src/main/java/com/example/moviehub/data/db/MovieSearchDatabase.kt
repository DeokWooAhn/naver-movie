package com.example.moviehub.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.moviehub.data.model.Item

@Database(
    entities = [Item::class],
    version = 1,
    exportSchema = false
)

abstract class MovieSearchDatabase : RoomDatabase() {

    abstract fun movieSearchDao(): MovieSearchDao

    companion object {
        @Volatile
        private var INSTANCE: MovieSearchDatabase? = null

        private fun buildDatabase(context: Context): MovieSearchDatabase =
            Room.databaseBuilder(
                context.applicationContext,
                MovieSearchDatabase::class.java,
                "favorite-movies"
            ).build()

        fun getInstance(context: Context): MovieSearchDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
    }
}