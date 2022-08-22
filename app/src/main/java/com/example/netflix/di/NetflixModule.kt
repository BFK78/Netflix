package com.example.netflix.di

import android.content.Context
import androidx.room.Room
import com.example.netflix.common.Constants.BASE_URL
import com.example.netflix.data.local.dao.CacheHelperDao
import com.example.netflix.data.local.dao.NetflixDao
import com.example.netflix.data.local.database.NetflixDatabase
import com.example.netflix.data.remote.api.NetflixApi
import com.example.netflix.data.remote.repository.GetMoviesImplementation
import com.example.netflix.domain.repository.GetMoviesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetflixModule {


    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): NetflixDatabase  {
        return Room.databaseBuilder(
            context,
            NetflixDatabase::class.java,
            "Netflix"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDataAccessObject(
        database: NetflixDatabase
    ): NetflixDao {
        return database.dao
    }

    @Provides
    @Singleton
    fun provideCacheDao(
        database: NetflixDatabase
    ): CacheHelperDao {
        return database.cacheDao
    }

    @Provides
    @Singleton
    fun provideRetrofit(): NetflixApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NetflixApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGetMoviesRepository(
        api: NetflixApi,
        dao: NetflixDao,
        cacheHelperDao: CacheHelperDao
    ): GetMoviesRepository {
        return GetMoviesImplementation(
            api = api,
            dao = dao,
            cacheDao = cacheHelperDao
        )
    }

}