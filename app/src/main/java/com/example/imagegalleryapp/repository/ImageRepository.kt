package com.example.imagegalleryapp.repository

import androidx.paging.PagingData
import com.example.imagegalleryapp.models.UnsplashImage
import com.example.imagegalleryapp.utils.SnackBarEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow

interface ImageRepository {

     fun getEditorialFeedImages(): Flow<PagingData<UnsplashImage>>

    suspend fun getImage(imageId:String): UnsplashImage

    suspend fun searchImages(query:String): Flow<PagingData<UnsplashImage>>

    suspend fun toggleFavoriteStatus(image: UnsplashImage, _snackBarEvent: Channel<SnackBarEvent>)

    fun getFavoriteImageIds(): Flow<List<String>>

    fun getAllFavoriteImages():Flow<PagingData<UnsplashImage>>
}