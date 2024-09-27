package com.example.imagegalleryapp.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.imagegalleryapp.data.UnsplashApiService
import com.example.imagegalleryapp.data.local.ImageGalleryDatabase
import com.example.imagegalleryapp.data.mapper.toDomainModel
import com.example.imagegalleryapp.data.mapper.toFavoriteImageEntity
import com.example.imagegalleryapp.data.mapper.toMyModel
import com.example.imagegalleryapp.data.paging.EditorialFeedRemoteMediator
import com.example.imagegalleryapp.data.paging.SearchPagingSource
import com.example.imagegalleryapp.models.UnsplashImage
import com.example.imagegalleryapp.utils.Constans.ITEMS_PER_PAGE
import com.example.imagegalleryapp.utils.SnackBarEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalPagingApi::class)
class ImageRepositoryImpl(
    private val unsplashApi: UnsplashApiService,
   private val database: ImageGalleryDatabase
): ImageRepository {

    private val favoriteImageDao = database.favoriteImagesDao()
    private val editorialImageDao = database.editorialFeedDao()



    override fun getEditorialFeedImages(): Flow<PagingData<UnsplashImage>> {

       return Pager(
           config = PagingConfig(pageSize = ITEMS_PER_PAGE),
           remoteMediator = EditorialFeedRemoteMediator(unsplashApi, database),
           pagingSourceFactory = {

               editorialImageDao.getAllEditorialFeedImages()

           }
       ).flow
           .map {  paginData->

              paginData.map { it.toDomainModel() }

           }
    }

    override suspend fun getImage(imageId: String): UnsplashImage {

        return unsplashApi.getImage(imageId).toMyModel()
    }

    override suspend fun searchImages(query: String): Flow<PagingData<UnsplashImage>> {

        return Pager(
            config = PagingConfig(pageSize = ITEMS_PER_PAGE),
            pagingSourceFactory = {

                SearchPagingSource(query,unsplashApi)

            }
        ).flow
    }

    override suspend fun toggleFavoriteStatus(
        image: UnsplashImage,
        _snackBarEvent: Channel<SnackBarEvent>
    ) {

        val isFavorite = favoriteImageDao.isImageFavorite(image.id)

        val favoriteImage = image.toFavoriteImageEntity()

        if(isFavorite){

            favoriteImageDao.deleteFavoriteImage(favoriteImage)
            _snackBarEvent.send(
                SnackBarEvent(message = "This Image was deleted from Favorite !")
            )

        }else {
            favoriteImageDao.insertFavoriteImage(favoriteImage)
            _snackBarEvent.send(
                SnackBarEvent(message = "This Image was added in Favorite !")
            )
        }


    }

    override fun getFavoriteImageIds(): Flow<List<String>> {

        return favoriteImageDao.getFavoriteImagesIds()
    }

    override fun getAllFavoriteImages(): Flow<PagingData<UnsplashImage>> {

        return Pager(
            config = PagingConfig(pageSize = ITEMS_PER_PAGE),
            pagingSourceFactory = { favoriteImageDao.getAllFavoriteImages() }
        )
            .flow
            .map { pagingData ->

                pagingData.map { it.toDomainModel() }
            }

    }

}