package com.example.imagegalleryapp.navigtion

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.imagegalleryapp.screens.FavoriteScreen
import com.example.imagegalleryapp.screens.fullImage.FullImageScreen
import com.example.imagegalleryapp.screens.ProfileScreen
import com.example.imagegalleryapp.screens.SearchScreen
import com.example.imagegalleryapp.screens.fullImage.FullImageViewModel
import com.example.imagegalleryapp.screens.home.HomeScreen
import com.example.imagegalleryapp.screens.home.HomeViewModel

@Composable
fun NavGraphSetup()
{

    val navController = rememberNavController()
    
    NavHost(navController = navController,
        startDestination = Routes.HomeScreen)
    {
        
        composable<Routes.HomeScreen> {

            val viewModel : HomeViewModel = hiltViewModel()
            
            HomeScreen(images = viewModel.images,
                navController = navController,
                onImageClick = { imageID->

                    navController.navigate(Routes.FullImageScreen(
                        imageId = imageID
                    ))
                })
            
        }

        composable<Routes.SearchScreen> {

            SearchScreen(navController = navController)

        }

        composable<Routes.FavoriteScreen> {

           FavoriteScreen(navController)

        }

        composable<Routes.FullImageScreen> {
            val fviewModel : FullImageViewModel = hiltViewModel()

            FullImageScreen(image = fviewModel.image,
                navController = navController)

        }

        composable<Routes.ProfileScreen> {

            val profileArgs = it.toRoute<Routes.ProfileScreen>()

            ProfileScreen(profileLink = profileArgs.profileLink,
                          navController)

        }


    }
    
    
}