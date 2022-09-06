package com.example.moviehub.ui.view

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviehub.databinding.FragmentSearchBinding
import com.example.moviehub.ui.adapter.MovieSearchPagingAdapter
import com.example.moviehub.ui.viewmodel.MovieSearchViewModel
import com.example.moviehub.util.Constants.SEARCH_MOVIES_TIME_DELAY
import com.example.moviehub.util.collectLatestStateFlow

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var movieSearchViewModel: MovieSearchViewModel

    //    private lateinit var movieSearchAdapter: MovieSearchAdapter
    private lateinit var movieSearchAdapter: MovieSearchPagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieSearchViewModel = (activity as MainActivity).movieSearchViewModel

        setupRecyclerView()
        searchMovies()
        setupLoadState()

//        movieSearchViewModel.searchResult.observe(viewLifecycleOwner) { response ->
//            val movies = response.items
//            movieSearchAdapter.submitList(movies)
//        }

        collectLatestStateFlow(movieSearchViewModel.searchPagingResult) {
            movieSearchAdapter.submitData(it)
        }
    }

    private fun setupRecyclerView() {
//        movieSearchAdapter = MovieSearchAdapter()
        movieSearchAdapter = MovieSearchPagingAdapter()
        binding.rvSearchResult.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = movieSearchAdapter
        }
        movieSearchAdapter.setOnItemClickListener {
            val action = SearchFragmentDirections.actionFragmentSearchToFragmentMovie(it)
            findNavController().navigate(action)
        }
    }

    private fun searchMovies() {
        var startTime = System.currentTimeMillis()
        var endTime: Long

        binding.etSearch.text =
            Editable.Factory.getInstance().newEditable(movieSearchViewModel.query)

        binding.etSearch.addTextChangedListener { text: Editable? ->
            endTime = System.currentTimeMillis()
            if (endTime - startTime >= SEARCH_MOVIES_TIME_DELAY) {
                text?.let {
                    val query = it.toString().trim()
                    if (query.isNotEmpty()) {
//                        movieSearchViewModel.searchMovies(query)
                        movieSearchViewModel.searchMoviesPaging(query)
                        movieSearchViewModel.query = query
                    }
                }
            }
            startTime = endTime
        }
    }

    private fun setupLoadState() {
        movieSearchAdapter.addLoadStateListener { combinedLoadStates ->
            val loadState = combinedLoadStates.source
            val isLisEmpty = movieSearchAdapter.itemCount < 1
                    && loadState.refresh is LoadState.NotLoading
                    && loadState.append.endOfPaginationReached

            binding.tvEmptylist.isVisible = isLisEmpty
            binding.rvSearchResult.isVisible = !isLisEmpty

            binding.progressBar.isVisible = loadState.refresh is LoadState.Loading

            binding.btnRetry.isVisible = loadState.refresh is LoadState.Error
                    || loadState.append is LoadState.Error
                    || loadState.prepend is LoadState.Error
            val errorState: LoadState.Error? = loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
                ?: loadState.refresh as? LoadState.Error
            errorState?.let {
                Toast.makeText(requireContext(), it.error.message, Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnRetry.setOnClickListener {
            movieSearchAdapter.retry()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}