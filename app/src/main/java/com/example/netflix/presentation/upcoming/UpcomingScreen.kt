package com.example.netflix.presentation.upcoming

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.netflix.common.Constants
import com.example.netflix.common.Screens
import com.example.netflix.presentation.homescreen.BottomNavigationBar
import com.example.netflix.presentation.homescreen.SlideItem
import com.example.netflix.presentation.homescreen.viewmodel.HomeScreenViewModel
import com.google.gson.Gson

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalCoilApi::class)
@Composable
fun UpcomingScreen(
    navHostController: NavHostController,
    viewModel: HomeScreenViewModel = hiltViewModel(),
) {
    
    val upcomingMovies = viewModel.upComingMovieState
    

        Scaffold(
            bottomBar =  {
                BottomNavigationBar(navHostController = navHostController)
            },
            modifier = Modifier.background(color = Color.Black)
        ) {
            if (!upcomingMovies.value.isLoading) {
                Column {
                    UpcomingTopBar {
                        navHostController.popBackStack()
                    }

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .background(color = Color.Black)
                    ) {
                        items(upcomingMovies.value.movieModel!!.results) {

                            val painter =
                                rememberImagePainter(data = "${Constants.BASE_IMAGE_URL}${it.poster_path}")

                            SlideItem(painter = painter, contentDescription = "") {
                                val model = it
                                val json = Uri.encode(Gson().toJson(model))
                                navHostController.navigate("Movie_Detail_Screen/$json")
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
}

@Composable
fun UpcomingTopBar(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(color = Color.Black),
        contentAlignment = Alignment.TopStart
    ) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "",
                tint = Color.White
            )
        }
    }
}