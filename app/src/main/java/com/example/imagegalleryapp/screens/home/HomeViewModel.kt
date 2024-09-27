package com.example.imagegalleryapp.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.imagegalleryapp.models.UnsplashImage
import com.example.imagegalleryapp.repository.ImageRepository
import com.example.imagegalleryapp.utils.SnackBarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ImageRepository
):ViewModel() {

    private val _snackBarEvent = Channel<SnackBarEvent>()
    val snackBarEvent = _snackBarEvent.receiveAsFlow()


    val images: StateFlow<PagingData<UnsplashImage>> = repository.getEditorialFeedImages()
        .cachedIn(viewModelScope)
        .catch {  exception ->

            _snackBarEvent.send(
                SnackBarEvent(message = "Something went wrong. ${exception.message}")
            )
        }
        .stateIn(

            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = PagingData.empty()
        )


    val favoriteImageIds: StateFlow<List<String>> = repository.getFavoriteImageIds()
        .catch {  exception ->

            _snackBarEvent.send(
                SnackBarEvent(message = "Something went wrong. ${exception.message}")
            )
        }
        .stateIn(

            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = emptyList()
        )


    fun toggleFavoriteStatus(image: UnsplashImage) {

        viewModelScope.launch {

            try {

                repository.toggleFavoriteStatus(image, _snackBarEvent)

            } catch (e:Exception){

                _snackBarEvent.send(
                    SnackBarEvent(message = "Something went wrong. ${e.message}")
                )

            }

        }

    }


}