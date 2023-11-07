package com.example.project_8_movie
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
/**
 * Singleton object to hold the Retrofit client instance for making API calls
 */
object RetrofitClient
{
    const val BASE_URL = "https://www.omdbapi.com/"

    /**
     * API service interface for Retrofit to define the GET requests for movie details
     */
    val retrofitService: OmdbApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OmdbApiService::class.java)
    }
}