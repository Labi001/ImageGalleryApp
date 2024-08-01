package com.example.imagegalleryapp.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.example.imagegalleryapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageGalleryTopAppBar(
    modifier: Modifier = Modifier,
    title: String = "Image Gallery",
    onSearchClick: () -> Unit = {},
    nestedScroll: TopAppBarScrollBehavior
){

    CenterAlignedTopAppBar(
        modifier = modifier,
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
        actions = {
            
            IconButton(onClick = { onSearchClick() }) {

                Icon(imageVector = Icons.Filled.Search,
                    contentDescription = stringResource(R.string.search_icon)
                )
                
            }
            
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(

            scrolledContainerColor = MaterialTheme.colorScheme.background

        ))

}