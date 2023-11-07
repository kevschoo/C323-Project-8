package com.example.project_8_movie

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.asLiveData
import retrofit2.HttpException
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException

class SharedViewModel(application: Application) : AndroidViewModel(application)
{
    val apiKey = "82fc201"

    val moviesData: MutableLiveData<List<Movie>?> = MutableLiveData()
    val errorMessage = MutableLiveData<String?>()
    val loadingStatus = MutableLiveData<Boolean>()
    val rawApiResponse = MutableLiveData<String?>()
    var searchMode = MutableLiveData<Boolean>(false)

    /**
     * Initiates a movie search with the given title. Updates LiveData based on response
     *
     * @param title the movie title to search for
     */
    fun searchMovie(title: String)
    {
        viewModelScope.launch {
            loadingStatus.value = true
            try
            {
                val response = if (searchMode.value == true)
                {
                    RetrofitClient.retrofitService.getMovieDetails(title, apiKey)
                } else
                {
                    RetrofitClient.retrofitService.getSingleMovieDetails(title, apiKey)
                }
                handleResponse(response)
            }
            catch (e: IOException)
            {
                errorMessage.value = "Check your internet connection"
                rawApiResponse.value = e.message
            } catch (e: HttpException)
            {
                errorMessage.value = "HTTP error: ${e.code()}"
                rawApiResponse.value = e.response()?.errorBody()?.string()
            } catch (e: Exception)
            {
                errorMessage.value = e.message ?: "An unknown error occurred"
                rawApiResponse.value = e.message
            } finally
            {
                loadingStatus.value = false
            }
        }
    }

    /**
     * Handles the response from the movie API call. Updates LiveData for movies or errors
     *
     * @param response the response object from Retrofit call
     */
    private fun handleResponse(response: Response<*>)
    {
        if (response.isSuccessful)
        {
            when (val responseBody = response.body())
            {
                is MovieApiResponse ->
                {
                    moviesData.value = responseBody.Search
                    errorMessage.value = null
                    rawApiResponse.value = responseBody.toString()
                }
                is Movie ->
                {
                    moviesData.value = listOf(responseBody)
                    errorMessage.value = null
                    rawApiResponse.value = responseBody.toString()
                }
                else ->
                {
                    errorMessage.value = "No movies found"
                    rawApiResponse.value = response.errorBody()?.string()
                }
            }
        } else
        {
            errorMessage.value = (response.body() as? MovieApiResponse)?.Error ?: "An unknown error occurred"
            rawApiResponse.value = response.errorBody()?.string()
        }
    }

    /**
     * Clears the movie list data and any error messages.
     */
    fun clearMovieListData()
    {
        moviesData.value = emptyList()
        errorMessage.value = null
    }

}