package com.example.netflix.presentation.homescreen.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netflix.common.Constants.API_KEY
import com.example.netflix.common.Constants.PAGE_NUMBER
import com.example.netflix.common.Resource
import com.example.netflix.domain.model.MovieModel
import com.example.netflix.domain.repository.GetMoviesRepository
import com.example.netflix.presentation.homescreen.state.MovieState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getMoviesRepository: GetMoviesRepository
): ViewModel() {

    private val _popularMovieState = mutableStateOf(MovieState())
    val popularMovieState: State<MovieState> = _popularMovieState

    private val _latestMovieState = mutableStateOf(MovieState())
    val latestMovieState: State<MovieState> = _latestMovieState

    private val _upComingMovieState = mutableStateOf(MovieState())
    val upComingMovieState: State<MovieState> = _upComingMovieState

    private val _nowPlayingMovieState = mutableStateOf(MovieState())
    val nowPlayingMovieState: State<MovieState> = _nowPlayingMovieState

    private val _topRatedMovieState = mutableStateOf(MovieState())
    val topRatedMovieState: State<MovieState> = _topRatedMovieState

    init {

        getPopularMovies(
            apiKey = API_KEY,
            page = PAGE_NUMBER
        )

        getLatestMovies(
            apiKey = API_KEY,
            page = PAGE_NUMBER
        )


        getNowPlayingMovies(
            apiKey = API_KEY,
            page = PAGE_NUMBER
        )

        getUpcomingMovies(
            apiKey = API_KEY,
            page = PAGE_NUMBER
        )

        getTopRatedMovies(
            apiKey = API_KEY,
            page = PAGE_NUMBER
        )

    }

    private fun getPopularMovies(apiKey: String, page: Int) = viewModelScope.launch {
        getMoviesRepository.getPopularMovies(
            apiKey = apiKey,
            page = page
        ).onEach {
            when (it) {
                is Resource.Loading -> {
                    _popularMovieState.value = popularMovieState.value.copy(
                        isLoading = true,
                        movieModel = it.data
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

    private fun getLatestMovies(apiKey: String, page: Int) = viewModelScope.launch {
        getMoviesRepository.getLatestMovies(
            apiKey = apiKey,
            page = page
        ).onEach {
            when (it) {
                is Resource.Loading -> {
                    _latestMovieState.value = latestMovieState.value.copy(
                        isLoading = true,
                        movieModel = it.data
                    )
                }
                is Resource.Success -> {
                    _latestMovieState.value = latestMovieState.value.copy(
                        isLoading = false,
                        movieModel = it.data
                    )
                }
                is Resource.Error -> {
                    _latestMovieState.value = latestMovieState.value.copy(
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }

    private fun getNowPlayingMovies(apiKey: String, page: Int) = viewModelScope.launch {
        getMoviesRepository.getNowPlayingMovies(
            apiKey = apiKey,
            page = page
        ).onEach {
            when (it) {
                is Resource.Loading -> {
                    _nowPlayingMovieState.value = nowPlayingMovieState.value.copy(
                        isLoading = true,
                        movieModel = it.data
                    )
                }
                is Resource.Success -> {
                    _nowPlayingMovieState.value = nowPlayingMovieState.value.copy(
                        isLoading = false,
                        movieModel = it.data
                    )
                }
                is Resource.Error -> {
                    _nowPlayingMovieState.value = nowPlayingMovieState.value.copy(
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }

    private fun getUpcomingMovies(apiKey: String, page: Int) = viewModelScope.launch {
        getMoviesRepository.getUpcomingMovies(
            apiKey = apiKey,
            page = page
        ).onEach {
            when (it) {
                is Resource.Loading -> {
                    _upComingMovieState.value = upComingMovieState.value.copy(
                        isLoading = true,
                        movieModel = it.data
                    )
                }
                is Resource.Success -> {
                    _upComingMovieState.value = upComingMovieState.value.copy(
                        isLoading = false,
                        movieModel = it.data
                    )
                }
                is Resource.Error -> {
                    _upComingMovieState.value = upComingMovieState.value.copy(
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }

    private fun getTopRatedMovies(apiKey: String, page: Int) = viewModelScope.launch {
        getMoviesRepository.getTopRatedMovies(
            apiKey = apiKey,
            page = page
        ).onEach {
            when (it) {
                is Resource.Loading -> {
                    _topRatedMovieState.value = topRatedMovieState.value.copy(
                        isLoading = true,
                        movieModel = it.data
                    )
                }
                is Resource.Success -> {
                    _topRatedMovieState.value = topRatedMovieState.value.copy(
                        isLoading = false,
                        movieModel = it.data
                    )
                }
                is Resource.Error -> {
                    _topRatedMovieState.value = topRatedMovieState.value.copy(
                        isLoading = false
                    )
                }
            }
        }.launchIn(this)
    }

}