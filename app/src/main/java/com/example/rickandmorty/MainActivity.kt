package com.example.rickandmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.network.KtorClient
import com.example.network.models.domain.Character
import com.example.rickandmorty.screens.CharacterDetailsScreen
import com.example.rickandmorty.screens.CharacterEpisodeScreen
import com.example.rickandmorty.ui.theme.RickAndMortyTheme
import com.example.rickandmorty.ui.theme.RickPrimary
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var ktorClient: KtorClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navController = rememberNavController()

            RickAndMortyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color =  RickPrimary
                ) {
                    NavHost(navController = navController, startDestination = "character_details") {
                        composable(route = "character_details") {
                            CharacterDetailsScreen(
                                characterId = 1
                            ) {
                                navController.navigate("character_episodes/$it")
                            }
                        }
                        composable(
                            route = "character_episodes/{characterId}",
                            arguments = listOf(navArgument("characterId") {
                                type = NavType.IntType
                            })
                        ) { backStackEntry ->
                            val characterId: Int =
                                backStackEntry.arguments?.getInt("characterId") ?: -1
                            CharacterEpisodeScreen(
                                characterId = characterId,
                                ktorClient = ktorClient
                            )
                        }
                    }
                }
            }
        }
    }
}