package com.example.imagegalleryapp.data.mapper

import com.example.imagegalleryapp.data.local.entities.FavoriteImageEntity
import com.example.imagegalleryapp.data.local.entities.UnsplashImageEntity
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

fun UnsplashImageDto.toEntity(): UnsplashImageEntity {
    return UnsplashImageEntity(

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


fun UnsplashImage.toFavoriteImageEntity(): FavoriteImageEntity {

    return FavoriteImageEntity(

        id = this.id,
        imageUrlSmall = this.imageUrlSmall,
        imageUrlRegular = this.imageUrlRegular,
        imageUrlRaw = this.imageUrlRaw,
        photographerName = this.photographerName,
        photographerUserName = this.photographerUserName,
        photographerProfileUrlImg = this.photographerProfileUrlImg,
        photographerProfileLink = this.photographerProfileLink,
        width = this.width,
        height = this.height,
        description = this.description


    )

}

fun FavoriteImageEntity.toDomainModel():UnsplashImage {

    return UnsplashImage(

        id = this.id,
        imageUrlSmall = this.imageUrlSmall,
        imageUrlRegular = this.imageUrlRegular,
        imageUrlRaw = this.imageUrlRaw,
        photographerName = this.photographerName,
        photographerUserName = this.photographerUserName,
        photographerProfileUrlImg = this.photographerProfileUrlImg,
        photographerProfileLink = this.photographerProfileLink,
        width = this.width,
        height = this.height,
        description = this.description
    )
}

fun UnsplashImageEntity.toDomainModel():UnsplashImage {

    return UnsplashImage(

        id = this.id,
        imageUrlSmall = this.imageUrlSmall,
        imageUrlRegular = this.imageUrlRegular,
        imageUrlRaw = this.imageUrlRaw,
        photographerName = this.photographerName,
        photographerUserName = this.photographerUserName,
        photographerProfileUrlImg = this.photographerProfileUrlImg,
        photographerProfileLink = this.photographerProfileLink,
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

fun List<UnsplashImageDto>.toEntityList(): List<UnsplashImageEntity> {

    return this.map {
        it.toEntity()
    }
}
