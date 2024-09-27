package com.example.imagegalleryapp.utils.networkCheck

import kotlinx.coroutines.flow.StateFlow

interface NetworkConnectivityObserver {

    val networkStatus: StateFlow<NetworkStatus>

}