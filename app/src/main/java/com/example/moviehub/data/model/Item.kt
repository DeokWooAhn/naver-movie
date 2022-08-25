package com.example.moviehub.data.model


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Item(
    @field:Json(name = "actor")
    val actor: String,
    @field:Json(name = "director")
    val director: String,
    @field:Json(name = "image")
    val image: String,
    @field:Json(name = "link")
    val link: String,
    @field:Json(name = "pubDate")
    val pubDate: String,
    @field:Json(name = "subtitle")
    val subtitle: String,
    @field:Json(name = "title")
    val title: String,
    @field:Json(name = "userRating")
    val userRating: String
) : Parcelable