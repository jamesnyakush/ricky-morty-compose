package com.jnyakush.rickymorty.ui.screen.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jnyakush.rickymorty.ui.viewmodel.CharacterViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun CharacterSearchBar(
    viewModel: CharacterViewModel = koinViewModel()
) {
    var search by remember { mutableStateOf("") }
    val filterState by viewModel.filters.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        OutlinedTextField(
            value = search,
            onValueChange = {
                search = it
                viewModel.searchCharactersFromInput(it)
            },
            label = { Text("Search (e.g. rick s:alive g:male sp:human)") },
            modifier = Modifier.fillMaxWidth()
        )

        FilterChipRow(filter = filterState) { newFilter ->
            viewModel.searchCharacters(
                name = newFilter.name,
                status = newFilter.status,
                species = newFilter.species,
                gender = newFilter.gender
            )
            // Update search box to reflect active filters
            search = buildString {
                newFilter.name?.let { append(it) }
                newFilter.status?.let { append(" s:$it") }
                newFilter.gender?.let { append(" g:$it") }
                newFilter.species?.let { append(" sp:$it") }
            }.trim()
        }
    }
}