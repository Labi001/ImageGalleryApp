package com.example.imagegalleryapp.repository

import com.example.imagegalleryapp.models.UnsplashImage

interface ImageRepository {

    suspend fun getEditorialFeedImages(): List<UnsplashImage>

    suspend fun getImage(imageId:String): UnsplashImage
}