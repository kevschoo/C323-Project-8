package com.example.project_8_movie

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApiService
{

    /**
     * Retrieves a list of movie details that match the given title
     *
     * @param title the movie title to search for
     * @param apiKey the API key required for requests
     * @return a Response object containing a list of movies or an error
     */
    @GET(".")
    suspend fun getMovieDetails(
        @Query("s") title: String,
        @Query("apikey") apiKey: String
    ): Response<MovieApiResponse>

    /**
     * Retrieves a single movie's details based on the given title
     *
     * @param title the movie title to search for
     * @param apiKey the API key required for requests
     * @return a Response object containing a single movie or an error
     */
    @GET(".")
    suspend fun getSingleMovieDetails(
        @Query("t") title: String,
        @Query("apikey") apiKey: String
    ): Response<Movie>
}