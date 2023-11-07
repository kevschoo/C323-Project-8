package com.example.project_8_movie

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project_8_movie.databinding.MovieListItemBinding
import android.net.Uri

/**
 * Adapter for the RecyclerView that displays a list of movies
 */
class MovieAdapter(private var movies: List<Movie>) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    /**
     * Provides a reference to the type of views that you are using (custom ViewHolder)
     */
    class MovieViewHolder(val binding: MovieListItemBinding) : RecyclerView.ViewHolder(binding.root)

    /**
     * Create new views
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to an adapter position
     * @param viewType The view type of the new View
     * @return A new ViewHolder that holds a View of the given view type
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder
    {
        val binding = MovieListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    /**
     * Replace the contents of a view
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the item at the given position in the data set
     * @param position The position of the item within the adapter's data set
     */
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int)
    {
        val movie = movies[position]
        with(holder.binding)
        {
            textTitle.text = movie.title ?: ""

            if (movie.year.isNullOrEmpty())
            {textYear.visibility = View.GONE}
            else
            {
                textYear.text = "Year: ${movie.year}"
                textYear.visibility = View.VISIBLE
            }

            if (movie.runtime.isNullOrEmpty()) {textRuntime.visibility = View.GONE}
            else
            {
                textRuntime.text = "Runtime: ${movie.runtime}"
                textRuntime.visibility = View.VISIBLE
            }

            if (movie.genre.isNullOrEmpty())
            {textGenre.visibility = View.GONE}
            else
            {
                textGenre.text = "Genre: ${movie.genre}"
                textGenre.visibility = View.VISIBLE
            }

            if (movie.imdbRating.isNullOrEmpty()) { textImdbRating.visibility = View.GONE}
            else
            {
                textImdbRating.text = "IMDb Rating: ${movie.imdbRating}"
                textImdbRating.visibility = View.VISIBLE
            }

            if (movie.ratings.isNullOrEmpty()) { textRatings.visibility = View.GONE }
            else
            {
                val ratingsText = movie.ratings.joinToString(separator = "\n") { rating -> "${rating.source}: ${rating.value}"}
                textRatings.text = ratingsText
                textRatings.visibility = View.VISIBLE
            }

            textImdbLink.text = holder.itemView.context.getString(R.string.imdb_page, movie.imdbID)
            textImdbLink.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.imdb.com/title/${movie.imdbID}/"))
                holder.itemView.context.startActivity(intent)
            }

            if (!movie.poster.isNullOrEmpty())
            {Glide.with(holder.itemView.context).load(movie.poster).into(imagePoster)}
            else
            {imagePoster.visibility = View.GONE}

            buttonShare.setOnClickListener {
                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "Check out this movie: ${movie.title ?: "N/A"}, https://www.imdb.com/title/${movie.imdbID}/")
                    type = "text/plain"
                }
                holder.itemView.context.startActivity(Intent.createChooser(shareIntent, "Share via"))
            }
        }
    }

    /**
     * Return the size of your dataset (invoked by the layout manager)
     *
     * @return The size of your dataset
     */
    override fun getItemCount() = movies.size

    /**
     * Updates the movies list and notifies the adapter to refresh the RecyclerView
     *
     * @param newMovies The new list of movies to be displayed
     */
    fun updateMovies(newMovies: List<Movie>)
    {
        movies = newMovies
        notifyDataSetChanged()
    }
}