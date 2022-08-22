package com.example.netflix.presentation.movie_detail.viewmodels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netflix.common.Constants.API_KEY
import com.example.netflix.common.Resource
import com.example.netflix.domain.model.Result
import com.example.netflix.domain.repository.GetMoviesRepository
import com.example.netflix.presentation.homescreen.state.MovieState
import com.example.netflix.presentation.movie_detail.state.TrailerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val getMoviesRepository: GetMoviesRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _similarMovieState = mutableStateOf(MovieState())
    val similarMovieState: State<MovieState> = _similarMovieState

    private val _movieTrailerState = mutableStateOf(TrailerState())
    val movieTrailerState: State<TrailerState> = _movieTrailerState

    private val result = savedStateHandle.get<Result>("result")

    init {
        getSimilarMovies(
            movieId = result!!.id!! ,
            apiKey = API_KEY
        )

        getMovieTrailer(
            movieId = result.id!!
        )
    }

    private fun getSimilarMovies(
        movieId: Int,
        apiKey: String
    ) = viewModelScope.launch {
        getMoviesRepository.getSimilarMovies(
            movieId = movieId,
            apiKey = apiKey
        ).onEach {
            when(it) {

                is Resource.Loading -> {
                    _similarMovieState.value = similarMovieState.value.copy(
                        isLoading = true
                    )
                }

                is Resource.Success -> {
                    _similarMovieState.value = similarMovieState.value.copy(
                        isLoading = false,
                        movieModel = it.data
                    )
                }

                is Resource.Error -> {
                    _similarMovieState.value = similarMovieState.value.copy(
                        isLoading = false
                    )
                }

            }
        }.launchIn(this)
    }

    private fun getMovieTrailer(
        movieId: Int,
        apiKey: String = API_KEY
    ) = viewModelScope.launch {
        getMoviesRepository.getMovieTrailer(
            movieId = movieId,
            apiKey = apiKey
        ).onEach {

            when (it) {

                is Resource.Loading -> {
                    _movieTrailerState.value = movieTrailerState.value.copy(
                        isLoading = true
                    )
                }

                is Resource.Success -> {
                    _movieTrailerState.value = movieTrailerState.value.copy(
                        isLoading = false,
                        trailerState = it.data
                    )
                }

                is Resource.Error -> {
                    _movieTrailerState.value = movieTrailerState.value.copy(
                        isLoading = false
                    )
                }

            }
        }.launchIn(this)
    }

}