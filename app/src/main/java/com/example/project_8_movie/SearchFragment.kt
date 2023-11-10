package com.example.project_8_movie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import com.example.project_8_movie.databinding.FragmentSearchBinding
import android.content.Intent
import android.net.Uri
import android.view.MenuItem
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager

class SearchFragment : Fragment()
{

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SharedViewModel by viewModels({requireActivity()})

    /**
     * Inflates the menu options from a menu resource
     *
     * @param menu The menu in which you place your items
     * @param inflater The inflater to use for inflating menu items
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_feedback_menu, menu)
    }

    /**
     * Inflates the view for this fragment
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here
     * @return The View for the fragment's UI
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        return binding.root
    }

    /**
     * Called immediately after onCreateView
     *
     * @param view The View returned by onCreateView(LayoutInflater, ViewGroup, Bundle)
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = "Movie Search"
        binding.searchButton.setOnClickListener {viewModel.searchMovie(binding.searchInput.text.toString())}

        viewModel.rawApiResponse.observe(viewLifecycleOwner) { rawJson ->rawJson?.let {binding.apiStatusText.text = it}}

        val movieAdapter = MovieAdapter(emptyList())
        binding.moviesRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.moviesRecyclerView.adapter = movieAdapter

        viewModel.moviesData.observe(viewLifecycleOwner) { movies ->movies?.let {movieAdapter.updateMovies(it)} }

        binding.searchModeSwitch.setOnCheckedChangeListener { _, isChecked -> viewModel.searchMode.value = isChecked}

        binding.searchInput.addTextChangedListener {binding.clearButton.visibility = if (it.toString().isNotEmpty()) View.VISIBLE else View.GONE }

        binding.clearButton.setOnClickListener {
            binding.searchInput.text.clear()
            viewModel.clearMovieListData()
            binding.apiStatusText.text = ""
            //binding.apiStatusText.visibility = View.GONE
        }
    }

    /**
     * Handles action item selections in the options menu
     *
     * @param item the menu item that was selected
     * @return boolean indicating if the action was handled
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        return when (item.itemId) {
            R.id.action_email -> {
                sendFeedbackEmail()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Composes and starts an email intent for sending feedback.
     */
    private fun sendFeedbackEmail() {
        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf("kevschoo@iu.edu"))
            putExtra(Intent.EXTRA_SUBJECT, "Feedback")
            putExtra(Intent.EXTRA_TEXT, "Enter your feedback here...")
        }

        try {
            startActivity(Intent.createChooser(emailIntent, "Send email using..."))
        } catch (e: Exception) {
            Toast.makeText(context, "No email app found", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Called when the view is destroyed
     */
    override fun onDestroyView()
    {
        super.onDestroyView()
        _binding = null
    }
}