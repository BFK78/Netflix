package com.example.netflix.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.netflix.domain.model.CacheHelper
import kotlinx.coroutines.flow.Flow

@Dao
interface CacheHelperDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCacheHelper(cacheHelper: CacheHelper)

    @Query("SELECT * FROM cachehelper WHERE id=1")
    suspend fun getCacheHelper(): CacheHelper

}