package com.example.netflix.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector


sealed class Screens(val title: String, val route: String, val icon: ImageVector? = null) {

    object HomeScreen: Screens("Home", "Home_Screen", Icons.Default.Home)
    object MovieDetailScreen: Screens("Movie Detail Screen", "Movie_Detail_Screen/{result}")
    object UpcomingScreen: Screens(title = "Upcoming", "Upcoming_Screen", icon = Icons.Default.List)
    object SearchScreen: Screens(title = "Search", route = "Search_Screen/{movieName}", icon = Icons.Default.Search)

}