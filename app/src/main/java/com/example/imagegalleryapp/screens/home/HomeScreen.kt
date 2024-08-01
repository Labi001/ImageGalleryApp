package com.example.imagegalleryapp.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.imagegalleryapp.R
import com.example.imagegalleryapp.components.ImageGalleryTopAppBar
import com.example.imagegalleryapp.components.ImageVerticalGrid
import com.example.imagegalleryapp.components.ZoomedImageCard
import com.example.imagegalleryapp.models.UnsplashImage
import com.example.imagegalleryapp.navigtion.Routes
import com.skydoves.cloudy.cloudy


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    images: List<UnsplashImage>,
    onImageClick: (String) -> Unit,
    navController: NavHostController,
){

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    var showImagePreview by remember { mutableStateOf(false) }

    var activeImage by remember { mutableStateOf<UnsplashImage?>(null) }

    Scaffold(
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
                } ,
                onClick = {
                    navController.navigate(Routes.FavoriteScreen)
                }) {

                Icon(painter = painterResource(R.drawable.ic_save),
                    contentDescription = "Favorite",
                    tint = MaterialTheme.colorScheme.onBackground)

            }

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
                        onImageDragEnd = {showImagePreview = false})


                }



            ZoomedImageCard(
                modifier = Modifier.padding(20.dp),
                isVisible = showImagePreview,
                image = activeImage)


        }



    }




}