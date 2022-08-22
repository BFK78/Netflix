package com.example.netflix.data.remote.repository

import android.util.Log
import com.example.netflix.common.Constants.LATEST
import com.example.netflix.common.Constants.PLAYING
import com.example.netflix.common.Constants.POPULAR
import com.example.netflix.common.Constants.RATED
import com.example.netflix.common.Constants.UPCOMING
import com.example.netflix.common.Resource
import com.example.netflix.common.compareHour
import com.example.netflix.data.local.dao.CacheHelperDao
import com.example.netflix.data.local.dao.NetflixDao
import com.example.netflix.data.remote.api.NetflixApi
import com.example.netflix.domain.model.CacheHelper
import com.example.netflix.domain.model.MovieModel
import com.example.netflix.domain.model.MovieVideo
import com.example.netflix.domain.repository.GetMoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import java.util.*

class GetMoviesImplementation(
    private val api: NetflixApi,
    private val dao: NetflixDao,
    private val cacheDao: CacheHelperDao
): GetMoviesRepository {

    override suspend fun getPopularMovies(apiKey: String, page: Int): Flow<Resource<MovieModel>> = flow {

        emit(Resource.Loading())

        val cachedResult = dao.getMovies(POPULAR)

        val cachedMovies = MovieModel(
            page = 1,
            results = cachedResult
        )

        emit(Resource.Loading(
            data = cachedMovies
        ))

        try {
            val previousHelper = cacheDao.getCacheHelper()

            Log.i("basim previous", previousHelper.toString())
            val request = compareHour(Date().toString(), previousHelper.lastTime)

            if (request) {

                val movieModel = api.getPopularMovies(api_key = apiKey, page = page)

                movieModel.results.forEach {
                    it.type = POPULAR
                }

                Log.i("basim api", movieModel.toString())

//            val cacheHelper = CacheHelper(lastTime = Date().toString())
//
//            cacheDao.insertCacheHelper(cacheHelper)
//
//            cacheDao.getCacheHelper().collectLatest {
//                Log.i("basim it", it.toString())
//            }

                val cacheHelper = CacheHelper(lastTime = Date().toString())

                cacheDao.insertCacheHelper(cacheHelper)

                dao.insertMovies(movieModel.results)

                emit(Resource.Success(data = movieModel))
            }
        } catch (e: Exception) {
            Log.i("basim cache", e.message.toString())
            cacheDao.insertCacheHelper(
                CacheHelper(
                    lastTime = Date().toString()
                )
            )

            Log.i("basim", e.message.toString())
            emit(Resource.Error(message = e.message.toString()))
        }

    }

    override suspend fun getLatestMovies(apiKey: String, page: Int): Flow<Resource<MovieModel>> = flow {

        emit(Resource.Loading())
        val cachedResult = dao.getMovies(LATEST)
        val cachedMovies = MovieModel(
            page = 1,
            results = cachedResult
        )
        emit(Resource.Loading(data = cachedMovies))
        try {
            val previousHelper = cacheDao.getCacheHelper()
            val request = compareHour(Date().toString(), previousHelper.lastTime)
            if (request) {
                val movieModel = api.getLatestMovies(api_key = apiKey, page = page)
                movieModel.results.forEach {
                    it.type = LATEST
                }
                val cacheHelper = CacheHelper(lastTime = Date().toString())
                cacheDao.insertCacheHelper(cacheHelper)
                dao.insertMovies(movieModel.results)
                emit(Resource.Success(data = movieModel))
            }
        } catch (e: Exception) {
            Log.i("basim cache", e.message.toString())
            cacheDao.insertCacheHelper(
                CacheHelper(
                    lastTime = Date().toString()
                )
            )
            emit(Resource.Error(message = e.message.toString()))
        }
    }

    override suspend fun getNowPlayingMovies(apiKey: String, page: Int): Flow<Resource<MovieModel>> = flow {

        emit(Resource.Loading())

        val cachedResult = dao.getMovies(PLAYING)

        val cachedMovies = MovieModel(
            page = 1,
            results = cachedResult
        )
        emit(Resource.Loading(data = cachedMovies))

        try {
            val previousHelper = cacheDao.getCacheHelper()
            val request = compareHour(Date().toString(), previousHelper.lastTime)
            if (request) {
                val movieModel = api.getNowPlayingMovies(api_key = apiKey, page = page)
                movieModel.results.forEach {
                    it.type = PLAYING
                }

                val cacheHelper = CacheHelper(lastTime = Date().toString())
                cacheDao.insertCacheHelper(cacheHelper)
                dao.insertMovies(movieModel.results)

                emit(Resource.Success(data = movieModel))
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message.toString()))
        }
    }

    override suspend fun getUpcomingMovies(apiKey: String, page: Int): Flow<Resource<MovieModel>> = flow {

        emit(Resource.Loading())

        val cachedResult = dao.getMovies(UPCOMING)

        val cachedMovie = MovieModel(
            page = 1,
            results = cachedResult
        )

        emit(Resource.Loading(data = cachedMovie))

        try {
            val previousHelper = cacheDao.getCacheHelper()
            val request = compareHour(Date().toString(), previousHelper.lastTime)
            if (request) {
                val movieModel = api.getUpcomingMovies(api_key = apiKey, page = page)
                movieModel.results.forEach {
                    it.type = UPCOMING
                }
                val cacheHelper = CacheHelper(lastTime = Date().toString())
                cacheDao.insertCacheHelper(cacheHelper)
                dao.insertMovies(movieModel.results)
                emit(Resource.Success(data = movieModel))
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message.toString()))
        }
    }

    override suspend fun getTopRatedMovies(apiKey: String, page: Int): Flow<Resource<MovieModel>> = flow {

        emit(Resource.Loading())

        val cachedResult = dao.getMovies(RATED)

        val cachedMovie = MovieModel(
            page = page,
            results = cachedResult
        )

        emit(Resource.Loading(cachedMovie))

        try {
            val previousHelper = cacheDao.getCacheHelper()
            val request = compareHour(Date().toString(), previousHelper.lastTime)
            if (request) {

                val movieModel = api.getTopRatedMovies(api_key = apiKey, page = page)
                movieModel.results.forEach {
                    it.type = RATED
                }
                val cacheHelper = CacheHelper(lastTime = Date().toString())
                cacheDao.insertCacheHelper(cacheHelper)
                dao.insertMovies(movieModel.results)
                emit(Resource.Success(data = movieModel))
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message.toString()))
        }
    }

    override suspend fun getSimilarMovies(
        movieId: Int,
        apiKey: String,
    ): Flow<Resource<MovieModel>> = flow {

        emit(Resource.Loading())

        try {
                val movieModel = api.getSimilarMovies(
                    movieId = movieId,
                    api_key = apiKey
                )
                emit(Resource.Success(movieModel))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message.toString()))
        }
    }

    override suspend fun searchMovies(apiKey: String, movie: String): Flow<Resource<MovieModel>> = flow {

        emit(Resource.Loading())

        try {
            val movieModel = api.searchMovies(
                api_key = apiKey,
                query = movie
            )

            emit(Resource.Success(movieModel))

        } catch (e: Exception) {
            emit(Resource.Error(message = e.message.toString()))
        }

    }

    override suspend fun getMovieTrailer(movieId: Int, apiKey: String): Flow<Resource<MovieVideo>> = flow {
        emit(Resource.Loading())
        try {
            val videoModel = api.getMovieTrailer(
                movieId = movieId,
                api_key = apiKey
            )
            emit(Resource.Success(data = videoModel))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message.toString()))
        }
    }
}
