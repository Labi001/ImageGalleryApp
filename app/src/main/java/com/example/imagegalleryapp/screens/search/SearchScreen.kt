package com.example.imagegalleryapp.screens.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import com.example.imagegalleryapp.components.ImageVerticalGrid
import com.example.imagegalleryapp.components.ZoomedImageCard
import com.example.imagegalleryapp.models.UnsplashImage
import com.example.imagegalleryapp.utils.SnackBarEvent
import com.example.imagegalleryapp.utils.searchKeyBoard
import com.skydoves.cloudy.cloudy
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    snackbarHostState: SnackbarHostState,
    snackBarEvent: Flow<SnackBarEvent>,
    searchedImages: LazyPagingItems<UnsplashImage>,
    favoriteImagesIds: List<String>,
    onsearch: (String) -> Unit,
    navController: NavController,
    onImageClick: (String) -> Unit,
    onToggleFavoriteStatus: (UnsplashImage) -> Unit,
    focusManager: FocusManager,
    keyboardController: SoftwareKeyboardController?,

) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var query by remember { mutableStateOf("") }

    var showImagePreview by remember { mutableStateOf(false) }

    var activeImage by remember { mutableStateOf<UnsplashImage?>(null) }

    var isSuggestionChipsVisible by remember {
        mutableStateOf(false)
    }

    val focusRequester = remember { FocusRequester() }



    LaunchedEffect(key1 = true) {

        snackBarEvent.collect{ event->

            snackbarHostState.showSnackbar(
                message = event.message,
                duration = event.duration
            )

        }

    }

    LaunchedEffect(key1 = Unit) {

        delay(500)
        focusRequester.requestFocus()


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

            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                scrollBehavior = scrollBehavior,
                title = {

                    SearchBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                            .onFocusChanged { isSuggestionChipsVisible = it.isFocused }
                            .padding(
                                horizontal = 10.dp,
                                vertical = 4.dp
                            ),
                        query = query,
                        onQueryChange = {query = it},
                        onSearch = {

                            onsearch(query)
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        },
                        placeholder = { Text(text = "Search...")},
                        leadingIcon = {

                            IconButton(onClick = { /*TODO*/ }) {

                                Icon(imageVector = Icons.Filled.Search,
                                    contentDescription = "Search Icon" )

                            }

                        },
                        trailingIcon = {

                            IconButton(onClick = {
                                if(query.isNotEmpty()) query = ""
                                else
                                    if(query.isEmpty()){

                                        keyboardController?.hide()
                                        focusManager.clearFocus()

                                        navController.navigateUp()
                                    }
                            }) {

                                Icon(imageVector = Icons.Filled.Close,
                                    contentDescription = "Close Icon")

                            }

                        },
                        active = false,
                        onActiveChange = {},
                        content = {})


                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ))


            
        },

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

                AnimatedVisibility(visible = isSuggestionChipsVisible) {

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(searchKeyBoard) { keyword->

                            SuggestionChip(
                                onClick = {

                                    onsearch(keyword)
                                    query = keyword
                                  keyboardController?.hide()
                                    focusManager.clearFocus()

                                },
                                label = {

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Start) {


                                        Icon(imageVector = Icons.Filled.Search,
                                            contentDescription = "Search Icon",
                                            tint = MaterialTheme.colorScheme.onPrimaryContainer)

                                        Spacer(modifier = Modifier.width(3.dp))

                                        Text(text = keyword)

                                    }

                                    },
                                colors = SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    labelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                ))


                        }
                    }

                }


                ImageVerticalGrid(images = searchedImages,
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


