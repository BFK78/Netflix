package com.example.netflix.presentation.search_screen.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netflix.common.Constants.API_KEY
import com.example.netflix.common.Resource
import com.example.netflix.domain.repository.GetMoviesRepository
import com.example.netflix.presentation.homescreen.state.MovieState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val getMoviesRepository: GetMoviesRepository
): ViewModel() {



    private val _popularMovieState = mutableStateOf(MovieState())
    val popularMovieState: State<MovieState> = _popularMovieState

    private val _state = mutableStateOf(MovieState())
    val state: State<MovieState> = _state

    init {

        getPopularMovies(
            apiKey = API_KEY,
            page = 1
        )

    }

    fun searchMovies(
        apiKey: String = API_KEY,
        movie: String
    ) = viewModelScope.launch {
        getMoviesRepository.searchMovies(
            apiKey = apiKey,
            movie = movie
        ).onEach {

            when(it) {

                is Resource.Loading -> {
                    _state.value = state.value.copy(
                        isLoading = true
                    )
                }

                is Resource.Success -> {
                    _state.value = state.value.copy(
                        isLoading = false,
                        movieModel = it.data
                    )
                    Log.i("tr", it.data.toString())
                }

                is Resource.Error -> {
                    _state.value = state.value.copy(
                        isLoading = false
                    )
                }
            }

        }.launchIn(this)
    }

    private fun getPopularMovies(apiKey: String, page: Int) = viewModelScope.launch {
        getMoviesRepository.getPopularMovies(
            apiKey = apiKey,
            page = page
        ).onEach {
            when (it) {
                is Resource.Loading -> {
                    _popularMovieState.value = popularMovieState.value.copy(
                        isLoading = true
                    )
                }
                is Resource.Success -> {
                    _popularMovieState.value = popularMovieState.value.copy(
                        isLoading = false,
                        movieModel = it.data
                    )
                }
                is Resource.Error -> {
                    _popularMovieState.value = popularMovieState.value.copy(
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }
}