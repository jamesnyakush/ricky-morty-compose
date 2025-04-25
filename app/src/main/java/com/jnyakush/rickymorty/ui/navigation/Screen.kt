package com.jnyakush.rickymorty.ui.navigation

sealed class Screen(val route: String) {
    object CharacterList : Screen("character_list")
    object CharacterDetail : Screen("character_detail/{characterId}") {
        fun createRoute(characterId: Int) = "character_detail/$characterId"
    }
}