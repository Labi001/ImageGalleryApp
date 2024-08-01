package com.example.imagegalleryapp.screens

import android.webkit.WebView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.imagegalleryapp.R


@Composable
fun ProfileScreen(profileLink: String,
                  navController: NavController){

    val context = LocalContext.current
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {

            ProfileTopAppBar {
                navController.navigateUp()
            }

        },
        containerColor = MaterialTheme.colorScheme.background) { innerPading->

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPading)) {


            AndroidView(
                factory = {
                    WebView(context).apply {
                        loadUrl(profileLink)
                    }
                })

        }

    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTopAppBar(
    onBackClick:() ->Unit
){

    TopAppBar(title = {
        Text(text = "Profile Screen")
    },
        navigationIcon = {

            IconButton(onClick = { onBackClick() }) {

                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back_icon)
                )

            }

        })


}