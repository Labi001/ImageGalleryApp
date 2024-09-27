package com.example.imagegalleryapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.imagegalleryapp.utils.Constans.FAVORITE_IMAGE_TABLE

@Entity(tableName = FAVORITE_IMAGE_TABLE)
data class FavoriteImageEntity(

    @PrimaryKey
    val id: String,
    val imageUrlSmall: String,
    val imageUrlRegular: String,
    val imageUrlRaw: String,
    val photographerName: String,
    val photographerUserName: String,
    val photographerProfileUrlImg: String,
    val photographerProfileLink: String,
    val width: Int,
    val height: Int,
    val description: String?

)
