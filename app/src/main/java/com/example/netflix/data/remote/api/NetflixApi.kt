package com.example.netflix.data.remote.api

import android.graphics.Movie
import com.example.netflix.domain.model.MovieModel
import com.example.netflix.domain.model.MovieVideo
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface NetflixApi {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") api_key: String,
        @Query("page") page: Int
    ): MovieModel

    @GET("tv/popular")
    suspend fun getLatestMovies(
        @Query("api_key") api_key: String,
        @Query("page") page: Int
    ): MovieModel

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("api_key") api_key: String,
        @Query("page") page: Int
    ): MovieModel

    @GET("tv/top_rated")
    suspend fun getNowPlayingMovies(
        @Query("api_key") api_key: String,
        @Query("page") page: Int
    ): MovieModel

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") api_key: String,
        @Query("page") page: Int
    ): MovieModel

    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovies(
        @Path("movie_id") movieId: Int,
        @Query("api_key") api_key: String,
    ): MovieModel

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("api_key") api_key: String,
        @Query("query") query: String
    ): MovieModel

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieTrailer(
        @Path("movie_id") movieId: Int,
        @Query("api_key") api_key: String,
    ): MovieVideo

}