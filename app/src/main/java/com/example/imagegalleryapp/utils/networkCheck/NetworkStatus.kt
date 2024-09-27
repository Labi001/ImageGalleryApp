package com.example.imagegalleryapp.utils.networkCheck

sealed class NetworkStatus {

    data object Connected: NetworkStatus()

    data object DisConnected: NetworkStatus()


}