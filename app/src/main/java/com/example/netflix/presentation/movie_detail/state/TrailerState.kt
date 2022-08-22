package com.example.netflix.presentation.movie_detail.state

import com.example.netflix.domain.model.MovieVideo

data class TrailerState (
    val isLoading: Boolean = true,
    val trailerState: MovieVideo? = null
)