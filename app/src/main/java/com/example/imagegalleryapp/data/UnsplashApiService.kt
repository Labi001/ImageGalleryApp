package com.example.imagegalleryapp.data

import com.example.imagegalleryapp.data.remote.UnsplashImageDto
import com.example.imagegalleryapp.data.remote.UnsplashImagesSearchResult
import com.example.imagegalleryapp.utils.Constans.API_KEY
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface UnsplashApiService {

    @Headers("Authorization: Client-ID $API_KEY")
    @GET("/photos")
    suspend fun getEditorialFeedImages(

        @Query("page") page: Int,
        @Query("per_page") perPage: Int,

    ): List<UnsplashImageDto>


    @Headers("Authorization: Client-ID $API_KEY")
    @GET("/photos/{id}")
    suspend fun getImage(
        @Path("id") imageId:String
    ): UnsplashImageDto


    @Headers("Authorization: Client-ID $API_KEY")
    @GET("/search/photos")
    suspend fun SearchImages(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,

    ) : UnsplashImagesSearchResult

}