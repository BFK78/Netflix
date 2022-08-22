package com.example.netflix.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.netflix.data.local.dao.CacheHelperDao
import com.example.netflix.data.local.dao.NetflixDao
import com.example.netflix.domain.model.CacheHelper
import com.example.netflix.domain.model.Result

@Database(
    entities = [Result::class, CacheHelper::class],
    version = 1
)
abstract class NetflixDatabase: RoomDatabase() {

    abstract val dao: NetflixDao
    abstract val cacheDao: CacheHelperDao

}