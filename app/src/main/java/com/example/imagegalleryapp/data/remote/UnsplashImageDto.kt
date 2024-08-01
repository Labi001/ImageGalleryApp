package com.example.imagegalleryapp.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class UnsplashImageDto(

    val description: String?,
    val height: Int,
    val id: String,
    val urls: Urls,
    val user: UserDto,
    val width: Int
)

@Serializable
data class Urls(
    val full: String,
    val raw: String,
    val regular: String,
    val small: String,
    val thumb: String
)