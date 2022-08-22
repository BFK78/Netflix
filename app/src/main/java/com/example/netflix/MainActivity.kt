package com.example.netflix

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.netflix.common.CustomNavType
import com.example.netflix.common.Screens
import com.example.netflix.domain.model.Result
import com.example.netflix.presentation.homescreen.HomeScreen
import com.example.netflix.presentation.movie_detail.MovieDetail
import com.example.netflix.presentation.search_screen.SearchScreen
import com.example.netflix.presentation.upcoming.UpcomingScreen
import com.example.netflix.ui.theme.NetflixTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {


            NetflixTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black
                ) {
                    val navHostController = rememberNavController()
                    NetflixNavigation(navHostController = navHostController, intent)

                }
            }
        }
    }
}

@Composable
fun NetflixNavigation(
    navHostController: NavHostController,
    intent: Intent
) {

    NavHost(navController = navHostController, startDestination = Screens.HomeScreen.route) {
        composable(Screens.HomeScreen.route) {
            HomeScreen(
                navHostController = navHostController,
                intent = intent
            )
        }

        composable(
            route = Screens.MovieDetailScreen.route,
            arguments = listOf(
                navArgument(name = "result") {
                    type = CustomNavType()
                }
            )
        ) {
            val result = it.arguments?.getParcelable<Result>("result")
            MovieDetail(
                result = result!!,
                navHostController = navHostController
            )
        }

        composable(
            route = Screens.UpcomingScreen.route
        ) {
            UpcomingScreen(navHostController = navHostController)
        }

        composable(
            route = Screens.SearchScreen.route,
            arguments = listOf(
                navArgument("movieName") { type = NavType.StringType }
            )
        ) {
            SearchScreen(navHostController = navHostController, movieName = it.arguments?.getString("movieName"))
        }
    }

}

