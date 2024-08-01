package com.example.imagegalleryapp.repository

import com.example.imagegalleryapp.data.UnsplashApiService
import com.example.imagegalleryapp.data.mapper.toMyModel
import com.example.imagegalleryapp.data.mapper.toMyModelList
import com.example.imagegalleryapp.models.UnsplashImage

class ImageRepositoryImpl(
    private val unsplashApi: UnsplashApiService
): ImageRepository {

    override suspend fun getEditorialFeedImages(): List<UnsplashImage> {

       return unsplashApi.getEditorialFeedImages().toMyModelList()
    }

    override suspend fun getImage(imageId: String): UnsplashImage {

        return unsplashApi.getImage(imageId).toMyModel()
    }

}