package com.example.moviehub.data.api

//object RetrofitInstance {
//    private val okHttpClient: OkHttpClient by lazy {
//        val httpLoggingInterceptor = HttpLoggingInterceptor()
//            .setLevel(HttpLoggingInterceptor.Level.BODY)
//        OkHttpClient.Builder()
//            .addInterceptor(httpLoggingInterceptor)
//            .build()
//    }
//
//    private val retrofit: Retrofit by lazy {
//        Retrofit.Builder()
//            .addConverterFactory(MoshiConverterFactory.create())
//            .client(okHttpClient)
//            .baseUrl(BASE_URL)
//            .build()
//    }
//
//    val api: MovieSearchApi by lazy {
//        retrofit.create(MovieSearchApi::class.java)
//    }
//}