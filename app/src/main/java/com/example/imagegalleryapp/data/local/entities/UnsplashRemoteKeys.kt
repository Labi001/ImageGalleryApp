package com.example.imagegalleryapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.imagegalleryapp.utils.Constans

@Entity(tableName = Constans.REMOTE_KEYS)
data class UnsplashRemoteKeys(

    @PrimaryKey
    val id: String,
    val prevPage: Int?,
    val nextPage: Int?

)
