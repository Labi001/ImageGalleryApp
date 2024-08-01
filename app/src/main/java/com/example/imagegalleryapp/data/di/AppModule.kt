package com.example.imagegalleryapp.data.di

import com.example.imagegalleryapp.data.UnsplashApiService
import com.example.imagegalleryapp.repository.ImageRepository
import com.example.imagegalleryapp.repository.ImageRepositoryImpl
import com.example.imagegalleryapp.utils.Constans
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideUnsplashApiService():UnsplashApiService {

         val contentType = "application/json".toMediaType()
         val json = Json { ignoreUnknownKeys = true }
         val retrofit = Retrofit.Builder()
            .addConverterFactory(json.asConverterFactory(contentType))
            .baseUrl(Constans.BASE_URL)
            .build()

        return retrofit.create(UnsplashApiService::class.java)

    }


    @Provides
    @Singleton
    fun provideImageRepository(
        apiService: UnsplashApiService
    ): ImageRepository{

        return ImageRepositoryImpl(apiService)
    }


}
