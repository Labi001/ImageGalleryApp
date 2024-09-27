package com.example.imagegalleryapp.data.di

import android.content.Context
import androidx.room.Room
import com.example.imagegalleryapp.data.UnsplashApiService
import com.example.imagegalleryapp.data.local.ImageGalleryDatabase
import com.example.imagegalleryapp.repository.ImageRepository
import com.example.imagegalleryapp.repository.ImageRepositoryImpl
import com.example.imagegalleryapp.repository.imageDownloader.AndroidImageDownloader
import com.example.imagegalleryapp.repository.imageDownloader.Downloader
import com.example.imagegalleryapp.utils.Constans
import com.example.imagegalleryapp.utils.Constans.IMAGE_GALLERY_DATABASE
import com.example.imagegalleryapp.utils.networkCheck.NetworkConnectivityObserver
import com.example.imagegalleryapp.utils.networkCheck.NetworkConnectivityObserverImpl
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
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
    fun provideImageGalleryDatabase(
        @ApplicationContext context: Context
    ): ImageGalleryDatabase{

        return Room.databaseBuilder(
            context,
            ImageGalleryDatabase::class.java,
           IMAGE_GALLERY_DATABASE
            )
            .build()
    }


    @Provides
    @Singleton
    fun provideImageRepository(
        apiService: UnsplashApiService,
        database: ImageGalleryDatabase
    ): ImageRepository{

        return ImageRepositoryImpl(apiService,database)
    }

    @Provides
    @Singleton
    fun provideImageDownloader(
        @ApplicationContext context: Context
    ) : Downloader{

       return AndroidImageDownloader(context)
    }


    @Provides
    @Singleton
    fun provideApplicationScope(): CoroutineScope {

        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }


    @Provides
    @Singleton
    fun provideNetworkConnectivityObserver(
        @ApplicationContext context: Context,
        scope: CoroutineScope
    ): NetworkConnectivityObserver {

        return NetworkConnectivityObserverImpl(
            context,
            scope

        )
    }


}
