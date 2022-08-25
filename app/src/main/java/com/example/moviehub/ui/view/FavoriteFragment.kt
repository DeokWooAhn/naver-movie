package com.example.moviehub.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviehub.databinding.FragmentFavoriteBinding
import com.example.moviehub.ui.adapter.MovieSearchAdapter
import com.example.moviehub.ui.viewmodel.MovieSearchViewModel
import com.google.android.material.snackbar.Snackbar

class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var movieSearchViewModel: MovieSearchViewModel
    private lateinit var movieSearchAdaper: MovieSearchAdapter

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
        movieSearchViewModel = (activity as MainActivity).movieSearchViewModel

        setupRecyclerView()
        setupTouchHelper(view)

        movieSearchViewModel.favoriteMovies.observe(viewLifecycleOwner) {
            movieSearchAdaper.submitList(it)
        }
    }

    private fun setupRecyclerView() {
        movieSearchAdaper = MovieSearchAdapter()
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
            adapter = movieSearchAdaper
        }
        movieSearchAdaper.setOnItemClickListener {
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
                val movie = movieSearchAdaper.currentList[position]
                movieSearchViewModel.deleteMovie(movie)
                Snackbar.make(view, "영화가 삭제되었습니다.", Snackbar.LENGTH_SHORT).apply {
                    setAction("취소") {
                        movieSearchViewModel.saveMovie(movie)
                    }
                }.show()
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