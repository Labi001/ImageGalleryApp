package com.example.imagegalleryapp.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class UnsplashImageDto(

    val description: String?,
    val height: Int,
    val id: String,
    val urls: UrlsDto,
    val user: UserDto,
    val width: Int
)

@Serializable
data class UrlsDto(
    val full: String,
    val raw: String,
    val regular: String,
    val small: String,
    val thumb: String
)