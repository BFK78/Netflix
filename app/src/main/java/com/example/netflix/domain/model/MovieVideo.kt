package com.example.netflix.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class MovieVideo(
    val id: Int,
    val results: List<VideoResult>
)