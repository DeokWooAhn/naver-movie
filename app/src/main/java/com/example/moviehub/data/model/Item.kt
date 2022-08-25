package com.example.moviehub.data.model


import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
@Entity(tableName = "movies")
data class Item(
    @field:Json(name = "actor")
    val actor: String,
    @field:Json(name = "director")
    val director: String,
    @field:Json(name = "image")
    val image: String,
    @PrimaryKey(autoGenerate = false)
    @field:Json(name = "link")
    val link: String,
    @ColumnInfo(name = "pub_data")
    @field:Json(name = "pubDate")
    val pubDate: String,
    @field:Json(name = "subtitle")
    val subtitle: String,
    @field:Json(name = "title")
    val title: String,
    @ColumnInfo(name = "user_rating")
    @field:Json(name = "userRating")
    val userRating: String
) : Parcelable