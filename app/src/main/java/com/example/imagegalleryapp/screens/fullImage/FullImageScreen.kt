package com.example.imagegalleryapp.screens.fullImage

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.animateZoomBy
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.imagegalleryapp.R
import com.example.imagegalleryapp.components.ImageLoadingBar
import com.example.imagegalleryapp.models.UnsplashImage
import com.example.imagegalleryapp.navigtion.Routes
import com.example.imagegalleryapp.utils.rememberWindowInsetsController
import com.example.imagegalleryapp.utils.toggleStatusBar
import kotlinx.coroutines.launch
import kotlin.math.max

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FullImageScreen(
    image: UnsplashImage?,
    navController: NavController,
    ){

    var showBars by rememberSaveable { mutableStateOf(false) }
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val scope = rememberCoroutineScope()

    val windowInsetsController = rememberWindowInsetsController()

    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {

            FullImageViewTopAppBar(
                image = image,
                onDownloadImgClick = {},
                onPhotographerImgClick = { profileImgLink ->

                    navController.navigate(Routes.ProfileScreen(
                        profileLink = profileImgLink
                    ))

                },
                onBackClick = {
                    navController.navigateUp()
                },
                isVisible = showBars
            )
        },
        containerColor = MaterialTheme.colorScheme.background)
    { innerPadding ->

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {


            
            LaunchedEffect(key1 = Unit) {

                windowInsetsController.toggleStatusBar(show = showBars)

                
            }

            BackHandler(enabled = !showBars) {

                windowInsetsController.toggleStatusBar(show = true)
                navController.navigateUp()

            }

            val isImageZoomed: Boolean by remember {
                derivedStateOf { scale != 1f }
            }

            val transformState = rememberTransformableState { zoomChange, offsetChange, _ ->
                val maxX = (constraints.maxWidth * (scale - 1)) / 2
                val maxY = (constraints.maxHeight * (scale - 1)) / 2
                scale = max( scale * zoomChange, 1f)
                offset = Offset(
                    x = (offset.x + offsetChange.x).coerceIn(-maxX, maxX),
                    y = (offset.y + offsetChange.y).coerceIn(-maxY, maxY)
                )

            }

            var isLoading by remember { mutableStateOf(true) }

            var isError by remember { mutableStateOf(true) }

            val imageLoader = rememberAsyncImagePainter(
                model = image?.imageUrlRaw,
                onState = { imageState ->

                    isLoading = imageState is AsyncImagePainter.State.Loading
                    isError = imageState is AsyncImagePainter.State.Error


                })

            if(isLoading){

                Box(modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center){

                    ImageLoadingBar()
                }



            }

            Box(modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center){




                Image(painter = if(isError.not()) imageLoader else {
                    painterResource(id = R.drawable.ic_error )
                },
                    contentDescription = null,
                    modifier = Modifier
                        .transformable(transformState)
                        .combinedClickable(
                            onDoubleClick = {

                                if (isImageZoomed) {

                                    scale = 1f
                                    offset = Offset.Zero

                                } else {

                                    scope.launch {
                                        transformState.animateZoomBy(zoomFactor = 3f)
                                    }


                                }

                            },
                            onClick = {

                                showBars = !showBars
                                windowInsetsController.toggleStatusBar(show = showBars)

                            },
                            indication = null,
                            interactionSource = remember {
                                MutableInteractionSource()
                            }
                        )
                        .graphicsLayer {

                            scaleX = scale
                            scaleY = scale
                            translationX = offset.x
                            translationY = offset.y


                        })


            }


        }

    }





}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullImageViewTopAppBar(
    image:UnsplashImage?,
    onPhotographerImgClick:(String) -> Unit,
    onDownloadImgClick:() -> Unit,
    onBackClick:() -> Unit,
    isVisible:Boolean

){

    AnimatedVisibility(visible = isVisible,
        enter = fadeIn() + slideInVertically (),
        exit =  fadeOut() + slideOutVertically(),
    ) {

        TopAppBar(
            title = {


            },
            navigationIcon = {


                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically) {

                    IconButton(onClick = { onBackClick() }) {

                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_icon)
                        )

                    }


                    IconButton(onClick = {
                        image?.let { onPhotographerImgClick(it.photographerProfileLink) }
                    }) {

                        AsyncImage(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape),
                            model = image?.photographerProfileUrlImg,
                            contentDescription = null)


                    }

                    Spacer(modifier = Modifier.width(3.dp))

                    Column {

                        Text(text = image?.photographerName ?: "",
                            style = MaterialTheme.typography.titleMedium)

                        Text(text = image?.photographerUserName ?: "",
                            style = MaterialTheme.typography.bodySmall)

                    }




                }



            },

            actions = {

                IconButton(onClick = { onDownloadImgClick() }) {

                    Icon(painter = painterResource(id = R.drawable.ic_download),
                        contentDescription = null)

                }




            })


    }



}