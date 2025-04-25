package com.jnyakush.rickymorty.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jnyakush.rickymorty.ui.screen.character.CharacterListScreen
import com.jnyakush.rickymorty.ui.screen.detail.CharacterDetailScreen

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController, startDestination = Screen.CharacterList.route) {
        composable(Screen.CharacterList.route) {
            CharacterListScreen(navController)
        }

        composable(
            route = Screen.CharacterDetail.route,
            arguments = listOf(navArgument("characterId") { type = NavType.IntType })
        ) { backStackEntry ->
            val characterId = backStackEntry.arguments?.getInt("characterId")
            characterId?.let {
                CharacterDetailScreen(characterId = it, navController)
            }
        }
    }
}