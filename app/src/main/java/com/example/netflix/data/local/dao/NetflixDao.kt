package com.example.netflix.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.netflix.domain.model.Result

@Dao
interface NetflixDao {

    @Query("SELECT * FROM RESULT WHERE type = :type")
    suspend fun getMovies(type: String): List<Result>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(list: List<Result>)

}