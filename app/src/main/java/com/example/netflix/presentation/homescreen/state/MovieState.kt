package com.example.netflix.presentation.homescreen.state

import com.example.netflix.domain.model.MovieModel

data class MovieState(
    val isLoading: Boolean = true,
    val movieModel: MovieModel? = null
)