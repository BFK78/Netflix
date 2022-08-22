package com.example.netflix.domain.repository

import com.example.netflix.common.Resource
import com.example.netflix.domain.model.MovieModel
import com.example.netflix.domain.model.MovieVideo
import kotlinx.coroutines.flow.Flow

interface GetMoviesRepository {

    suspend fun getPopularMovies(
        apiKey: String,
        page: Int
    ): Flow<Resource<MovieModel>>

    suspend fun getLatestMovies(
        apiKey: String,
        page: Int
    ): Flow<Resource<MovieModel>>

    suspend fun getNowPlayingMovies(
        apiKey: String,
        page: Int
    ): Flow<Resource<MovieModel>>

    suspend fun getUpcomingMovies(
        apiKey: String,
        page: Int
    ): Flow<Resource<MovieModel>>

    suspend fun getTopRatedMovies(
        apiKey: String,
        page: Int
    ): Flow<Resource<MovieModel>>

    suspend fun getSimilarMovies(
        movieId: Int,
        apiKey: String
    ): Flow<Resource<MovieModel>>

    suspend fun searchMovies(
        apiKey: String,
        movie: String
    ): Flow<Resource<MovieModel>>

    suspend fun getMovieTrailer(
        movieId: Int,
        apiKey: String
    ): Flow<Resource<MovieVideo>>

}