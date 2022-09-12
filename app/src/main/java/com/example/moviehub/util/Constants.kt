package com.example.moviehub.util

import com.example.moviehub.BuildConfig

object Constants {
    const val BASE_URL = "https://openapi.naver.com/v1/"
    const val API_ID = BuildConfig.movieApiId
    const val API_PW = BuildConfig.movieApiPw
    const val SEARCH_MOVIES_TIME_DELAY = 100L
    const val PAGING_SIZE = 15
    const val DATASTORE_NAME = "preferences_datastore"
}