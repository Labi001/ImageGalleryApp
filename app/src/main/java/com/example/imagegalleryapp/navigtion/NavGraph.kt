package com.example.imagegalleryapp.navigtion

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.imagegalleryapp.screens.ProfileScreen
import com.example.imagegalleryapp.screens.favorite.FavoriteScreen
import com.example.imagegalleryapp.screens.favorite.FavoriteViewModel
import com.example.imagegalleryapp.screens.fullImage.FullImageScreen
import com.example.imagegalleryapp.screens.fullImage.FullImageViewModel
import com.example.imagegalleryapp.screens.home.HomeScreen
import com.example.imagegalleryapp.screens.home.HomeViewModel
import com.example.imagegalleryapp.screens.search.SearchScreen
import com.example.imagegalleryapp.screens.search.SearchViewModel
import com.example.imagegalleryapp.utils.networkCheck.NetworkConnectivityObserver

@Composable
fun NavGraphSetup(
    connectivityObserver: NetworkConnectivityObserver,
    snackbarHostState: SnackbarHostState)
{

    val navController = rememberNavController()

    
    NavHost(navController = navController,
        startDestination = Routes.HomeScreen)
    {
        
        composable<Routes.HomeScreen> (

            enterTransition=::slideInToRight ,
            exitTransition = ::slideOutToLeft

        ) {

            val viewModel : HomeViewModel = hiltViewModel()
            val images = viewModel.images.collectAsLazyPagingItems()
            val favoriteImageIds by viewModel.favoriteImageIds.collectAsStateWithLifecycle()
            
            HomeScreen(
                snackbarHostState = snackbarHostState,
                snackBarEvent = viewModel.snackBarEvent,
                images = images,
                navController = navController,
                onImageClick = { imageID->

                    navController.navigate(Routes.FullImageScreen(
                        imageId = imageID
                    ))
                },
                connectivityObserver = connectivityObserver,
                onToggleFavoriteStatus = { image ->

                    viewModel.toggleFavoriteStatus(image)
                },
                favoriteImagesIds = favoriteImageIds)
            
        }

        composable<Routes.SearchScreen>(

            enterTransition = ::slideInToLeft,
            exitTransition = ::slideOutToLeft,
            popEnterTransition = ::slideInToRight,
            popExitTransition = ::slideOutToRight


        ) {
            val sviewModel : SearchViewModel = hiltViewModel()
            val searchImages = sviewModel.searchImages.collectAsLazyPagingItems()
            val favoriteImageIds by sviewModel.favoriteImageIds.collectAsStateWithLifecycle()

            val focusManager = LocalFocusManager.current
            val keyboardController = LocalSoftwareKeyboardController.current


            SearchScreen(
                snackbarHostState = snackbarHostState,
                snackBarEvent = sviewModel.snackBarEvent,
                searchedImages = searchImages,
                favoriteImagesIds = favoriteImageIds,
                onsearch = {

                    sviewModel.searchImages(it)
                },
                navController = navController,
                onImageClick = { imageID->

                    keyboardController?.hide()
                    focusManager.clearFocus()

                    navController.navigate(Routes.FullImageScreen(
                        imageId = imageID
                    ))

                },
                onToggleFavoriteStatus = { image ->

                 sviewModel.toggleFavoriteStatus(image)
                },
                focusManager,
                keyboardController,
               )

        }

        composable<Routes.FavoriteScreen>(

            enterTransition = ::slideInToLeft,
            exitTransition = ::slideOutToLeft,
            popEnterTransition = ::slideInToRight,
            popExitTransition = ::slideOutToRight

        ) {

            val favoriteViewModel : FavoriteViewModel = hiltViewModel()
            val allFavoriteImages = favoriteViewModel.allFavoriteImages.collectAsLazyPagingItems()
            val favoriteImageIds by favoriteViewModel.favoriteImageIds.collectAsStateWithLifecycle()

           FavoriteScreen(
               snackbarHostState = snackbarHostState,
               snackBarEvent = favoriteViewModel.snackBarEvent,
               favoriteImages = allFavoriteImages,
               favoriteImagesIds = favoriteImageIds,
               navController = navController,
               onImageClick = { imageID->
                   navController.navigate(Routes.FullImageScreen(
                       imageId = imageID
                   ))

               },
               onToggleFavoriteStatus = { image ->

                   favoriteViewModel.toggleFavoriteStatus(image)
               }
           )

        }

        composable<Routes.FullImageScreen>(

            enterTransition = ::slideInToLeft,
            exitTransition = ::slideOutToLeft,
            popEnterTransition = ::slideInToRight,
            popExitTransition = ::slideOutToRight

        ) {
            val fviewModel : FullImageViewModel = hiltViewModel()

            FullImageScreen(
                snackbarHostState = snackbarHostState,
                snackBarEvent = fviewModel.snackBarEvent,
                image = fviewModel.image,
                navController = navController,
                onImageDownloadClick = { url,title ->

                    fviewModel.downloadImage(url,title)

                })

        }

        composable<Routes.ProfileScreen>(

            enterTransition = ::slideInToLeft,
            exitTransition = ::slideOutToRight

        ) {

            val profileArgs = it.toRoute<Routes.ProfileScreen>()

            ProfileScreen(profileLink = profileArgs.profileLink,
                          navController)

        }


    }


    
}

fun slideInToLeft(scope:AnimatedContentTransitionScope<NavBackStackEntry>): EnterTransition {

    return scope.slideIntoContainer(

        AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(700)
    )

}

fun slideInToRight(scope:AnimatedContentTransitionScope<NavBackStackEntry>): EnterTransition {

    return scope.slideIntoContainer(

        AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(700)
    )

}

fun slideOutToLeft(scope:AnimatedContentTransitionScope<NavBackStackEntry>): ExitTransition {

    return scope.slideOutOfContainer(

        AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(700)
    )

}

fun slideOutToRight(scope:AnimatedContentTransitionScope<NavBackStackEntry>): ExitTransition {

    return scope.slideOutOfContainer(

        AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(700)
    )

}


