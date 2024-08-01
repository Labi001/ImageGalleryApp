package com.example.imagegalleryapp.data.mapper

import com.example.imagegalleryapp.data.remote.UnsplashImageDto
import com.example.imagegalleryapp.models.UnsplashImage

fun UnsplashImageDto.toMyModel(): UnsplashImage {
    return UnsplashImage(

        id = this.id,
        imageUrlSmall = this.urls.small,
        imageUrlRegular = this.urls.regular,
        imageUrlRaw = this.urls.raw,
        photographerName = this.user.name,
        photographerUserName = this.user.username,
        photographerProfileUrlImg = this.user.profileImage.small,
        photographerProfileLink = this.user.links.html,
        width = this.width,
        height = this.height,
        description = this.description

    )

}

fun List<UnsplashImageDto>.toMyModelList(): List<UnsplashImage> {

    return this.map {
        it.toMyModel()
    }
}
