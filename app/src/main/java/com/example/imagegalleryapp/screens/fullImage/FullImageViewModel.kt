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
import com.example.imagegalleryapp.repository.imageDownloader.Downloader
import com.example.imagegalleryapp.utils.SnackBarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class FullImageViewModel @Inject constructor(
    private val repository: ImageRepository,
    private val downloader: Downloader,
    savedStateHandle: SavedStateHandle
):ViewModel() {

    private val imageId = savedStateHandle.toRoute<Routes.FullImageScreen>().imageId

    private val _snackBarEvent = Channel<SnackBarEvent>()
    val snackBarEvent = _snackBarEvent.receiveAsFlow()

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
            }catch (e:UnknownHostException){

                _snackBarEvent.send(
                    SnackBarEvent(message = "No Internet connection.Please check your network.")
                )
            }

            catch (e:Exception){

               _snackBarEvent.send(
                   SnackBarEvent(message = "Something went wrong: ${e.message}")
               )
            }

        }

    }

   fun downloadImage(url:String,title:String?) {

       viewModelScope.launch {

           try {

               downloader.downloadFile(url,title)
           }catch (e:Exception) {


               _snackBarEvent.send(
                   SnackBarEvent(message = "Something went wrong: ${e.message}")
               )
           }

       }

   }


}