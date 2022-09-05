package com.example.moviehub.data.api

import com.example.moviehub.data.model.SearchResponse
import com.example.moviehub.util.Constants.API_ID
import com.example.moviehub.util.Constants.API_PW
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface MovieSearchApi {
    @Headers(
        "Host: openapi.naver.com",
        "User-Agent: curl/7.49.1",
        "Accept: */*",
        "X-Naver-Client-Id: $API_ID",
        "X-Naver-Client-Secret: $API_PW"
    )
    @GET("search/movie.json")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("display") display: Int
    ): Response<SearchResponse>
}