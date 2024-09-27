package com.example.imagegalleryapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.imagegalleryapp.data.local.dao.EditorialFeedDao
import com.example.imagegalleryapp.data.local.dao.FavoriteImagesDao
import com.example.imagegalleryapp.data.local.entities.FavoriteImageEntity
import com.example.imagegalleryapp.data.local.entities.UnsplashImageEntity
import com.example.imagegalleryapp.data.local.entities.UnsplashRemoteKeys

@Database(
    entities = [FavoriteImageEntity::class, UnsplashImageEntity::class, UnsplashRemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class ImageGalleryDatabase: RoomDatabase() {

  abstract fun favoriteImagesDao(): FavoriteImagesDao

  abstract fun editorialFeedDao(): EditorialFeedDao

}