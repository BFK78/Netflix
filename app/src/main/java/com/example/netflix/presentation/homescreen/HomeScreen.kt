package com.example.netflix.presentation.homescreen

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.netflix.R
import com.example.netflix.common.Constants.BASE_IMAGE_URL
import com.example.netflix.common.Screens
import com.example.netflix.domain.model.Result
import com.example.netflix.presentation.homescreen.viewmodel.HomeScreenViewModel
import com.google.gson.Gson


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    navHostController: NavHostController,
    intent: Intent
) {

    val deepLinkId = remember {
        mutableStateOf("")
    }

    val uri = intent.data

    if (uri != null) {

        val parameters = uri.pathSegments

        val param = parameters[parameters.size - 1]

        deepLinkId.value = param

    }

    LaunchedEffect(key1 = deepLinkId.value) {

        if (deepLinkId.value != "") {
            navHostController.navigate("Search_Screen/" "${deepLinkId}")
        }

    }

    val topHeight = 200.dp
    val topHeightPx = with(LocalDensity.current) {
        topHeight.roundToPx().toFloat()
    }

    val topOffsetPx = remember { mutableStateOf(0f) }

    val nestedScrollState = remember {
        object: NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = topOffsetPx.value + delta
                topOffsetPx.value = newOffset.coerceIn(
                    -topHeightPx,0f
                )
                return Offset.Zero
            }
        }
    }

    val verticalScrollState = rememberScrollState()
    val nowPlayingMovies = viewModel.nowPlayingMovieState
    val popularMovies = viewModel.popularMovieState
    val topRatedMovies = viewModel.topRatedMovieState
    val latestMovies = viewModel.latestMovieState

    Log.i("basim popular", popularMovies.toString())

    Scaffold(
        modifier = Modifier.background(Black),
        bottomBar = {
            BottomNavigationBar(
                navHostController = navHostController
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Black)
                .nestedScroll(connection = nestedScrollState)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(
                        state = verticalScrollState,
                        enabled = true
                    )
            ) {
                MainImageContainer(viewModel = viewModel)

                Spacer(modifier = Modifier.height(12.dp))

                MovieSlide(
                    text = "Popular",
                    list = popularMovies.value.movieModel?.results,
                    navHostController = navHostController
                )

                Spacer(modifier = Modifier.height(12.dp))

                MovieSlide(
                    text = "Top Rated",
                    list = topRatedMovies.value.movieModel?.results,
                    navHostController = navHostController
                )

                Spacer(modifier = Modifier.height(12.dp))

                MovieSlide(
                    text = "Tv Popular",
                    list = latestMovies.value.movieModel?.results,
                    navHostController = navHostController
                )

                Spacer(modifier = Modifier.height(12.dp))

                MovieSlide(
                    text = "Tv Latest",
                    list = nowPlayingMovies.value.movieModel?.results,
                    navHostController = navHostController
                )

                Spacer(modifier = Modifier.height(72.dp))

            }

            NetflixTopBar(modifier = Modifier.align(Alignment.TopCenter),
                translateY = topOffsetPx.value
            )
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun MainImageContainer(
    viewModel: HomeScreenViewModel
) {

    val painter = rememberImagePainter(data = "${BASE_IMAGE_URL}/jrgifaYeUtTnaH7NF5Drkgjg2MB.jpg") {
        crossfade(500)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Black)
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(550.dp),
            painter = painter,
            contentDescription = "",
            contentScale = ContentScale.FillWidth
        )
        Text(
            text = "Violent . Psychological . Thriller",
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = White
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            CIconButton(
                icon = Icons.Default.Add,
                contentDescription = "Add",
                text = "My List"
            )

            Row(
                modifier = Modifier
                    .background(color = White)
                    .padding(end = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = Black
                )

                Text(
                    text = "Play",
                    color = Black,
                    fontWeight = FontWeight.Bold
                )
            }

            CIconButton(
                icon = Icons.Outlined.Info,
                contentDescription = "Info",
                text = "Info"
            )
        }
    }
}


@OptIn(ExperimentalCoilApi::class)
@Composable
fun MovieSlide(
    text: String,
    list: List<Result>? = emptyList(),
    navHostController: NavHostController
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Black)
    ) {

        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            color = White,
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        if (list.isNullOrEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyRow(
                modifier = Modifier
            ) {
                items(list) {

                    val painter = rememberImagePainter(data = "$BASE_IMAGE_URL${it.poster_path}")

                    SlideItem(
                        painter = painter,
                        contentDescription = ""
                    ) {
                        val model = it
                        val json = Uri.encode(Gson().toJson(model))
                        navHostController.navigate("Movie_Detail_Screen/$json")
                    }
                }
            }
        }
    }
}


@Composable
fun SlideItem(
    painter: Painter,
    contentDescription: String,
    top: Boolean = false,
    new: Boolean = false,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(150.dp)
            .height(200.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .padding(horizontal = 4.dp)
            .clickable {
                onClick()
            }
            .padding(bottom = 8.dp)
    ) {
        Image(
            painter = painter,
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        if (top) {
            CustomBox(
                modifier = Modifier.align(Alignment.TopEnd)
            )
            if (new) {
                Box(
                    modifier = Modifier
                        .background(Color.Red)
                        .width(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "New Episodes",
                        color = White
                    )
                }
            }
        }
    }
}

@Composable
fun CIconButton(
    icon: ImageVector,
    contentDescription: String,
    text: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = White
        )

        Text(
            text = text,
            color = White
        )
    }
}

@Composable
fun CustomBox(
    modifier: Modifier
) {

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .size(48.dp)
        ) {
            drawPath(
                path = Path().let {
                    it.moveTo(0f, 0f)
                    it.lineTo(size.width, 0f)
                    it.lineTo(size.width, size.height)
                    it.lineTo(0f, size.height - 40f)
                    it.close()
                    it
                },
                color = Color.Red
            )
        }
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = White,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append("TOP")
                }

                withStyle(
                    style = SpanStyle(
                        color = White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                ){
                    append("\n10")
                }
            }
        )
    }
}

@Composable
fun NetflixTopBar(
    modifier: Modifier,
    translateY: Float
) {
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .graphicsLayer {
                this.translationY = translateY
            }
            .background(brush = Brush.verticalGradient(listOf(Black, Transparent)))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_netflix_seeklogo_com),
                    contentDescription = "",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Red
                )
            }

            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "",
                    tint = White
                )
            }
            
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "TV Shows",
                color = White
            )
            Text(
                text = "Movies",
                color = White
            )
            Text(
                text = "Categories",
                color = White
            )
        }
    }
}

@Composable
fun BottomNavigationBar(
    navHostController: NavHostController
) {
    val items = listOf(
        Screens.HomeScreen,
        Screens.UpcomingScreen,
        Screens.SearchScreen
    )
    
    BottomNavigation(
        backgroundColor = Black,
        contentColor = White
    ) {

        items.forEach {
            BottomNavigationItem(
                icon = { Icon(imageVector = it.icon!!, contentDescription = "") },
                label = { Text(text = it.title) },
                selectedContentColor = White,
                unselectedContentColor = White.copy(0.4f),
                alwaysShowLabel = true,
                selected = false,
                onClick = {

                    navHostController.navigate(it.route) {

                        navHostController.graph.startDestinationRoute?.let {
                            popUpTo(it) {
                                saveState = true
                            }
                        }

                        launchSingleTop = true

                        restoreState  = true

                    }
                }
            )
        }

    }
}
