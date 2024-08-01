
package com.example.imagegalleryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.imagegalleryapp.navigtion.NavGraphSetup
import com.example.imagegalleryapp.ui.theme.ImageGalleryAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            ImageGalleryAppTheme {

                NavGraphSetup()


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