package com.example.netflix.presentation.search_screen

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.netflix.common.Constants
import com.example.netflix.common.Constants.BASE_IMAGE_URL
import com.example.netflix.presentation.homescreen.BottomNavigationBar
import com.example.netflix.presentation.homescreen.SlideItem
import com.example.netflix.presentation.search_screen.viewmodel.SearchScreenViewModel
import com.google.gson.Gson

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalCoilApi::class)
@Composable
fun SearchScreen(
    viewModel: SearchScreenViewModel = hiltViewModel(),
    navHostController: NavHostController,
    movieName: String?
) {

    val popularMovies = viewModel.popularMovieState

    val text = remember {
        mutableStateOf(movieName?: "")
    }

    val movieState = viewModel.state

    LaunchedEffect(key1 = true) {
        if (movieName != null) {
            viewModel.searchMovies(
                movie = movieName
            )
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black),
        bottomBar = {
            BottomNavigationBar(
                navHostController = navHostController
            )
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black)
        ) {

            TextField(
                value = text.value,
                textStyle = TextStyle(
                    color = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth(),
                onValueChange = {
                    text.value = it
                    viewModel.searchMovies(
                        movie = text.value
                    )
                },
                placeholder = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Icon",
                            tint = Color.White
                        )
                        Text(
                            text = "Search",
                            color = Color.White,
                        )
                    }
                },
                trailingIcon = {
                    if (movieState.value.isLoading && text.value.isNotEmpty()) {
                        CircularProgressIndicator(
                            color = Color.Red,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Gray.copy(
                        alpha = 0.3f
                    ),
                    focusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (text.value.isEmpty()) { 
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Text(
                        text = "Popular Searches",
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        popularMovies.value.movieModel?.let {
                            items(it.results) { result ->

                                val painter =
                                    rememberImagePainter(data = "$BASE_IMAGE_URL${result.poster_path}") {
                                        crossfade(500)
                                    }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = painter,
                                        contentDescription = "",
                                        modifier = Modifier.clickable {
                                            val json = Uri.encode(Gson().toJson(result))
                                            navHostController.navigate("Movie_Detail_Screen/$json")
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Text(
                                        text = result.title.toString(),
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(32.dp))

                                }
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }
                }
            } else {
                LazyVerticalGrid(columns = GridCells.Fixed(3)) {
                    movieState.value.movieModel?.let {
                        items(it.results) { result ->

                            val painter =
                                rememberImagePainter(data = "${Constants.BASE_IMAGE_URL}${result.poster_path}")

                            SlideItem(
                                painter = painter,
                                contentDescription = "Search Item"
                            ) {
                                val json = Uri.encode(Gson().toJson(result))
                                navHostController.navigate("Movie_Detail_Screen/$json")
                            }
                        }
                    }
                }
            }
        }
    }
}

