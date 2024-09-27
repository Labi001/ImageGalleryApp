
package com.example.imagegalleryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.imagegalleryapp.navigtion.NavGraphSetup
import com.example.imagegalleryapp.ui.theme.ImageGalleryAppTheme
import com.example.imagegalleryapp.utils.networkCheck.NetworkConnectivityObserver
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var connectivityObserver: NetworkConnectivityObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {

            ImageGalleryAppTheme {

                val snackbarHostState = remember {
                    SnackbarHostState()
                }

                NavGraphSetup(
                    connectivityObserver,
                    snackbarHostState = snackbarHostState
                )


            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ImageGalleryAppTheme {
    }
}