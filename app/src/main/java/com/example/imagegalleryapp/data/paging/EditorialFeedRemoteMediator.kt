package com.example.imagegalleryapp.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.imagegalleryapp.data.UnsplashApiService
import com.example.imagegalleryapp.data.local.ImageGalleryDatabase
import com.example.imagegalleryapp.data.local.entities.UnsplashImageEntity
import com.example.imagegalleryapp.data.local.entities.UnsplashRemoteKeys
import com.example.imagegalleryapp.data.mapper.toEntityList
import com.example.imagegalleryapp.utils.Constans.ITEMS_PER_PAGE

@OptIn(ExperimentalPagingApi::class)
class EditorialFeedRemoteMediator(

    private val apiService: UnsplashApiService,
    private val database: ImageGalleryDatabase

): RemoteMediator<Int, UnsplashImageEntity>() {

    private val editorialFeedDao = database.editorialFeedDao()

    companion object {

        private const val STARTING_PAGE_INDEX = 1

    }


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UnsplashImageEntity>
    ): MediatorResult {

        try {

            val currentPage = when(loadType) {

                LoadType.REFRESH -> {

                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: STARTING_PAGE_INDEX

                }

                LoadType.PREPEND -> {

                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevPage
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    prevPage


                }
                LoadType.APPEND -> {

                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextPage
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    nextPage


                }
            }

            val response =
                apiService.getEditorialFeedImages(page = currentPage, perPage = ITEMS_PER_PAGE)
            val endOfPaginationReached = response.isEmpty()

            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if(endOfPaginationReached) null else currentPage + 1


            database.withTransaction {

                if(loadType == LoadType.REFRESH){

                    editorialFeedDao.deleteAllEditorialFeedImages()
                    editorialFeedDao.deleteAllRemoteKeys()

                }
                
                val remoteKeys = response.map { unsplashImageDto ->  

                    UnsplashRemoteKeys(

                        id = unsplashImageDto.id,
                        prevPage = prevPage,
                        nextPage = nextPage

                    )

                }

                editorialFeedDao.insertAllRemoteKeys(remoteKeys)
                editorialFeedDao.insertEditorialFeedImages(response.toEntityList())

            }

              return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        }catch (e:Exception) {

            return MediatorResult.Error(e)
        }


    }

    private suspend fun getRemoteKeyClosestToCurrentPosition (
        state: PagingState<Int, UnsplashImageEntity>
    ) : UnsplashRemoteKeys ?{

        return state.anchorPosition?.let { position ->

            state.closestItemToPosition(position)?.id?.let {  id->
                editorialFeedDao.getRemoteKeys(id = id)
            }

        }

    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int,
            UnsplashImageEntity>): UnsplashRemoteKeys? {

        return state.pages.firstOrNull{it.data.isNotEmpty()}?.data?.firstOrNull()
            ?.let { unsplashImage ->
                editorialFeedDao.getRemoteKeys(id = unsplashImage.id)
            }

    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int,
            UnsplashImageEntity>): UnsplashRemoteKeys? {

        return state.pages.lastOrNull(){it.data.isNotEmpty()}?.data?.lastOrNull()
            ?.let { unsplashImage ->
                editorialFeedDao.getRemoteKeys(id = unsplashImage.id)
            }

    }

}