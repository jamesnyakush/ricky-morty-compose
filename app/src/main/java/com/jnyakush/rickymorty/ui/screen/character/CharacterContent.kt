package com.jnyakush.rickymorty.ui.screen.character

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.compose.LazyPagingItems
import com.jnyakush.rickymorty.data.model.Character
import com.jnyakush.rickymorty.ui.navigation.Screen

@Composable
fun CharacterContent(
    characters: LazyPagingItems<Character>,
    navController: NavHostController
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(characters.itemCount) { index ->
            characters[index]?.let { character ->
                CharacterCard(character = character) {
                    navController.navigate(
                        Screen.CharacterDetail.createRoute(
                            character.id
                        )
                    )
                }
            }
        }
    }
}
