package com.example.imagegalleryapp.screens.favorite

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import com.example.imagegalleryapp.R
import com.example.imagegalleryapp.components.ImageVerticalGrid
import com.example.imagegalleryapp.components.ZoomedImageCard
import com.example.imagegalleryapp.models.UnsplashImage
import com.example.imagegalleryapp.utils.SnackBarEvent
import com.skydoves.cloudy.cloudy
import kotlinx.coroutines.flow.Flow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(snackbarHostState: SnackbarHostState,
                   snackBarEvent: Flow<SnackBarEvent>,
                   favoriteImages: LazyPagingItems<UnsplashImage>,
                   favoriteImagesIds: List<String>,
                   navController: NavController,
                   onImageClick: (String) -> Unit,
                   onToggleFavoriteStatus: (UnsplashImage) -> Unit) {



    var showImagePreview by remember { mutableStateOf(false) }

    var activeImage by remember { mutableStateOf<UnsplashImage?>(null) }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    LaunchedEffect(key1 = true) {

        snackBarEvent.collect{ event->

            snackbarHostState.showSnackbar(
                message = event.message,
                duration = event.duration
            )

        }

    }



    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState,
            snackbar = {
                Snackbar(snackbarData = it,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer)
            }) },
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.background,

        topBar = {

            FavoriteTopAppBar(
                navController = navController,
                nestedScroll = scrollBehavior
            )

        },

        ) { innerpading->


        Box(modifier = Modifier.fillMaxSize()){

            if(favoriteImages.itemCount == 0){

                EmptyState(
                    modifier = Modifier.fillMaxSize()
                        .padding(16.dp)
                )


            }


            Column ( modifier =
            if(showImagePreview){
                Modifier
                    .fillMaxSize()
                    .cloudy(radius = 25)
                    .padding(innerpading)

            }else {
                Modifier
                    .fillMaxSize()
                    .padding(innerpading)
            },

                horizontalAlignment = Alignment.CenterHorizontally) {


                ImageVerticalGrid(images = favoriteImages,
                    onImageClick = onImageClick,
                    onImageDragStart = { image->
                        activeImage = image
                        showImagePreview = true
                    },
                    onImageDragEnd = {showImagePreview = false},
                    onToggleFavoriteStatus = onToggleFavoriteStatus,
                    favoriteImageIds = favoriteImagesIds)


            }



            ZoomedImageCard(
                modifier = Modifier.padding(20.dp),
                isVisible = showImagePreview,
                image = activeImage)


        }



    }



}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteTopAppBar(navController: NavController,
                      nestedScroll: TopAppBarScrollBehavior,
                      title: String = "Favorite Images",) {

    CenterAlignedTopAppBar(
        modifier = Modifier.fillMaxWidth(),
        scrollBehavior = nestedScroll,
        title = {
            Text(text = buildAnnotatedString {

                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)){

                    append(title.split(" ").first())

                }

                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.secondary)){

                    append(" ${title.split(" ").last()}")

                }

            },
                fontWeight = FontWeight.ExtraBold)

        },
        navigationIcon = {

            IconButton(onClick = { navController.navigateUp() }) {

                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.back_icon))

            }

        }
      ,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(

            scrolledContainerColor = MaterialTheme.colorScheme.background

        ))


}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {

    Column(modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        Spacer(modifier = Modifier.height(48.dp))

        Image(
            modifier = Modifier.fillMaxWidth()
                .size(65.dp),
            painter = painterResource(R.drawable.ic_empty),
            contentDescription = "Empty Icon",
            colorFilter = ColorFilter.tint(
                color = MaterialTheme.colorScheme.primary
            ))

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "No Saved Images",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
                .copy(fontWeight = FontWeight.Bold))

    }


}
