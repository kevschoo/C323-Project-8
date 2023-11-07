package com.example.project_8_movie

/**
 * Data model class to capture the JSON response for a movie search query.
 */
data class MovieApiResponse(
    val Search: List<Movie>?,
    val totalResults: String?,
    val Response: String,
    val Error: String?
)
