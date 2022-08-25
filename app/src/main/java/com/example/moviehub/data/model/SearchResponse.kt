package com.example.moviehub.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResponse(
    @field:Json(name = "display")
    val display: Int,
    @field:Json(name = "items")
    val items: List<Item>,
    @field:Json(name = "lastBuildDate")
    val lastBuildDate: String,
    @field:Json(name = "start")
    val start: Int,
    @field:Json(name = "total")
    val total: Int,

//    val item: List<Item>
)