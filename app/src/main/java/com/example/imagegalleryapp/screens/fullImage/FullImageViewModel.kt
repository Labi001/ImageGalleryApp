package com.example.imagegalleryapp.screens.fullImage

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.imagegalleryapp.models.UnsplashImage
import com.example.imagegalleryapp.navigtion.Routes
import com.example.imagegalleryapp.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FullImageViewModel @Inject constructor(
    private val repository: ImageRepository,
    savedStateHandle: SavedStateHandle
):ViewModel() {

    private val imageId = savedStateHandle.toRoute<Routes.FullImageScreen>().imageId

    var image: UnsplashImage? by mutableStateOf(null)
        private set

    init {
        getImage()
    }


    private fun getImage() {

        viewModelScope.launch {

            try {
                val result = repository.getImage(imageId)

                image = result
            }catch (e:Exception){

                e.printStackTrace()
            }

        }

    }


}