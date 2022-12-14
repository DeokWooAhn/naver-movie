package com.example.moviehub.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviehub.databinding.FragmentFavoriteBinding
import com.example.moviehub.ui.adapter.MovieSearchPagingAdapter
import com.example.moviehub.ui.viewmodel.FavoriteViewModel
import com.example.moviehub.util.collectLatestStateFlow
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    //    private lateinit var movieSearchViewModel: MovieSearchViewModel
//    private val movieSearchViewModel by activityViewModels<MovieSearchViewModel>()
    private val favoriteViewModel by viewModels<FavoriteViewModel>()

    //    private lateinit var movieSearchAdapter: MovieSearchAdapter
    private lateinit var movieSearchAdapter: MovieSearchPagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        movieSearchViewModel = (activity as MainActivity).movieSearchViewModel

        setupRecyclerView()
        setupTouchHelper(view)

//        movieSearchViewModel.favoriteMovies.observe(viewLifecycleOwner) {
//            movieSearchAdaper.submitList(it)
//        }

//        lifecycleScope.launch {
//            movieSearchViewModel.favoriteMovies.collectLatest {
//                movieSearchAdaper.submitList(it)
//            }
//        }

//        viewLifecycleOwner.lifecycleScope.launch {
//            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                movieSearchViewModel.favoriteMovies.collectLatest {
//                    movieSearchAdaper.submitList(it)
//                }
//            }
//        }

//        collectLatestStateFlow(movieSearchViewModel.favoriteMovies) {
//            movieSearchAdapter.submitList(it)
//        }

        collectLatestStateFlow(favoriteViewModel.favoritePagingMovies) {
            movieSearchAdapter.submitData(it)
        }

    }

    private fun setupRecyclerView() {
//        movieSearchAdapter = MovieSearchAdapter()
        movieSearchAdapter = MovieSearchPagingAdapter()
        binding.rvFavoriteMovies.apply {
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
            val action = FavoriteFragmentDirections.actionFragmentFavoriteToFragmentMovie(it)
            findNavController().navigate(action)
        }
    }

    private fun setupTouchHelper(view: View) {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
//                val movie = movieSearchAdaper.currentList[position]
//                movieSearchViewModel.deleteMovie(movie)
//                Snackbar.make(view, "????????? ?????????????????????.", Snackbar.LENGTH_SHORT).apply {
//                    setAction("??????") {
//                        movieSearchViewModel.saveMovie(movie)
//                    }
//                }.show()

                val pagedMovie = movieSearchAdapter.peek(position)
                pagedMovie?.let { movie ->
                    favoriteViewModel.deleteMovie(movie)
                    Snackbar.make(view, "????????? ?????????????????????.", Snackbar.LENGTH_SHORT).apply {
                        setAction("??????") {
                            favoriteViewModel.saveMovie(movie)
                        }
                    }.show()
                }
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvFavoriteMovies)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}