package com.example.imagegalleryapp.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imagegalleryapp.models.UnsplashImage
import com.example.imagegalleryapp.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ImageRepository
):ViewModel() {

    var images: List<UnsplashImage> by mutableStateOf(emptyList())
        private set

    init {

        getImages()
    }

    private fun getImages() {

        viewModelScope.launch {

            val result = repository.getEditorialFeedImages()

            images = result

        }

    }
}