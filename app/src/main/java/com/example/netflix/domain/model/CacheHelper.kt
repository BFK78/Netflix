package com.example.netflix.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CacheHelper(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 1,
    val lastTime: String
)