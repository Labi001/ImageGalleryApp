package com.example.imagegalleryapp.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.compose.LazyPagingItems
import com.example.imagegalleryapp.R
import com.example.imagegalleryapp.components.ImageGalleryTopAppBar
import com.example.imagegalleryapp.components.ImageVerticalGrid
import com.example.imagegalleryapp.components.ZoomedImageCard
import com.example.imagegalleryapp.components.networkStatusBar
import com.example.imagegalleryapp.models.UnsplashImage
import com.example.imagegalleryapp.navigtion.Routes
import com.example.imagegalleryapp.utils.SnackBarEvent
import com.example.imagegalleryapp.utils.networkCheck.NetworkConnectivityObserver
import com.example.imagegalleryapp.utils.networkCheck.NetworkStatus
import com.skydoves.cloudy.cloudy
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    snackbarHostState: SnackbarHostState,
    snackBarEvent: Flow<SnackBarEvent>,
    images: LazyPagingItems<UnsplashImage>,
    onImageClick: (String) -> Unit,
    navController: NavHostController,
    connectivityObserver: NetworkConnectivityObserver,
    onToggleFavoriteStatus: (UnsplashImage) -> Unit,
    favoriteImagesIds: List<String>
){


    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    var showImagePreview by remember { mutableStateOf(false) }

    var activeImage by remember { mutableStateOf<UnsplashImage?>(null) }



    val status by connectivityObserver.networkStatus.collectAsState()

    var showMessageBar by rememberSaveable { mutableStateOf(false) }
    var message by rememberSaveable { mutableStateOf("") }
    var backgroundColor by remember { mutableStateOf(Color.Red) }

    LaunchedEffect(key1 = true) {

        snackBarEvent.collect{ event->

            snackbarHostState.showSnackbar(
                message = event.message,
                duration = event.duration
            )

        }

    }
    
    LaunchedEffect(key1 = status) {

        when(status){

            NetworkStatus.Connected -> {

                message = "Network Available"
                backgroundColor = Color.Green
                delay(2000)
                showMessageBar = false


            }
            NetworkStatus.DisConnected -> {

                showMessageBar = true
                message = "Internet Disconnected"
                backgroundColor = Color.Red

            }
        }
        
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState,
            snackbar = {
                Snackbar(snackbarData = it,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.fillMaxWidth().wrapContentHeight(Alignment.Bottom))
            },
            modifier = Modifier.fillMaxWidth().wrapContentHeight(Alignment.Bottom))},
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.background,

        topBar = {

            ImageGalleryTopAppBar(
                onSearchClick = {

                    navController.navigate(Routes.SearchScreen)
                },
                nestedScroll = scrollBehavior
            )
        },

        floatingActionButton = {


            FloatingActionButton(
                modifier = if(showImagePreview){
                    Modifier
                        .cloudy(radius = 25)
                        .padding(24.dp)
                }else{

                    Modifier
                        .padding(24.dp)
                },

                onClick = {
                    navController.navigate(Routes.FavoriteScreen)
                }) {

                Icon(painter = painterResource(R.drawable.ic_save),
                    contentDescription = "Favorite",
                    tint = MaterialTheme.colorScheme.onBackground)

            }

        },

        bottomBar = {

         networkStatusBar(
             isConnected = showMessageBar,
             message = message,
             backgroundColor = backgroundColor)


        }

    ) { innerpading->


        Box(modifier = Modifier.fillMaxSize()){


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


                    ImageVerticalGrid(images = images,
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