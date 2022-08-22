package com.example.netflix.presentation.movie_detail

import android.content.Intent
import android.net.Uri
import android.widget.Space
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.example.netflix.common.Constants.BASE_IMAGE_URL
import com.example.netflix.domain.model.Result
import com.example.netflix.presentation.homescreen.HomeScreen
import com.example.netflix.presentation.homescreen.SlideItem
import com.example.netflix.presentation.movie_detail.viewmodels.MovieDetailViewModel
import com.google.gson.Gson

@Composable
fun MovieDetail(
    result: Result,
    viewModel: MovieDetailViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    BottomSheetContent(
        model = result,
        viewModel = viewModel,
        navHostController = navHostController
    )
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun BottomSheetContent(
    model: Result,
    viewModel: MovieDetailViewModel,
    navHostController: NavHostController
) {

    val trailerState = viewModel.movieTrailerState

    val context = LocalContext.current

    val state = rememberScrollState()

    val similarMovies = viewModel.similarMovieState

    val painter = rememberImagePainter(data = "$BASE_IMAGE_URL${model.backdrop_path}") {
        crossfade(500)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = state)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {

                Image(
                    painter = painter,
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clickable {
                            trailerState.value.trailerState?.let {
                                val intent = Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://www.youtube.com/watch?v=${it.results[0].key}"))
                                context.startActivity(intent)
                            }
                        },
                    contentScale = ContentScale.Crop
                )

                IconButton(
                    onClick = { navHostController.popBackStack() },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .clip(shape = CircleShape)
                        .background(Color.Black)
                        .padding(4.dp)
                        .then(Modifier.size(24.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "",
                        tint = White,
                        modifier = Modifier
                    )
                }

                Box(
                    modifier = Modifier.fillMaxSize()
                        .background(color = Color.Gray.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = {
                        trailerState.value.trailerState?.let {
                            val intent = Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://www.youtube.com/watch?v=${it.results[0].key}"))
                            context.startActivity(intent)
                        }
                    },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(color = Color.Gray)

                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "",
                            modifier = Modifier
                                .size(32.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = model.title.toString(),
                fontWeight = FontWeight.Bold,
                color = White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = model.release_date.toString(),
                color = White,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            BottomSheetButton(
                buttonBgColor = White,
                textColor = Color.Black,
                icon = Icons.Default.PlayArrow,
                text = "Play"
            )

            Spacer(modifier = Modifier.height(8.dp))

            BottomSheetButton(
                buttonBgColor = Color.Gray,
                textColor = White,
                icon = Icons.Default.KeyboardArrowDown,
                text = "Download"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = model.overview.toString(),
                color = White,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
            ) {

                Spacer(modifier = Modifier.width(16.dp))

                BottomSheetIcon(
                    icon = Icons.Default.Add,
                    text = "My List"
                )

                Spacer(modifier = Modifier.width(32.dp))

                BottomSheetIcon(
                    icon = Icons.Outlined.ThumbUp,
                    text = "Rate"
                )

                Spacer(modifier = Modifier.width(24.dp))

                BottomSheetIcon(
                    icon = Icons.Default.Share,
                    text = "Share"
                )

            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "More Like This",
                color = White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (similarMovies.value.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyRow {
                    similarMovies.value.movieModel?.results?.let {
                        items(it) {
                            val p = rememberImagePainter(data = "$BASE_IMAGE_URL${it.poster_path}")
                            SlideItem(
                                painter = p,
                                contentDescription = ""
                            ) {
                                val modelr = it
                                val json = Uri.encode(Gson().toJson(modelr))
                                navHostController.popBackStack()
                                navHostController.navigate("Movie_Detail_Screen/$json")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

        }
    }
}

@Composable
fun BottomSheetIcon(
    icon: ImageVector,
    text: String
) {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = icon,
                contentDescription = "",
                tint = White,
                modifier = Modifier
                    .size(32.dp)
            )
        }

        Text(
            text = text,
            color = White
        )
    }
}

@Composable
fun BottomSheetButton(
    buttonBgColor: Color,
    textColor: Color,
    icon: ImageVector,
    text: String
) {

    Button(
        onClick = { /*TODO*/ },
        modifier = Modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = buttonBgColor.copy(alpha = 0.4f)
        )
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = icon,
                    contentDescription = "",
                    tint = textColor
                )
            }

            Text(
                text = text,
                color = textColor,
                fontWeight = FontWeight.Bold
            )
        }
    }

}