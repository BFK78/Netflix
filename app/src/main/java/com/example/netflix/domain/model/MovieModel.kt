package com.example.netflix.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class MovieModel(
    val page: Int,
    val results: List<Result>,

)