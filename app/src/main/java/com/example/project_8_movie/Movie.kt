package com.example.project_8_movie

import com.google.gson.annotations.SerializedName
/**
 * Data class representing a movie with all its details
 */
data class Movie(
    @SerializedName("Title") val title: String,
    @SerializedName("Year") val year: String,
    @SerializedName("Runtime") val runtime: String?,
    @SerializedName("Genre") val genre: String?,
    @SerializedName("Poster") val poster: String,
    @SerializedName("Ratings") val ratings: List<Rating>?,
    @SerializedName("imdbRating") val imdbRating: String?,
    @SerializedName("imdbID") val imdbID: String
)

/**
 * Data class representing a movie rating by a source and its value
 */
data class Rating(
    @SerializedName("Source") val source: String,
    @SerializedName("Value") val value: String
)