package com.jnyakush.rickymorty.ui.screen.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jnyakush.rickymorty.data.model.FilterChipData
import com.jnyakush.rickymorty.ui.viewmodel.CharacterFilter

@Composable
fun FilterChipRow(
    filter: CharacterFilter,
    onFilterRemoved: (CharacterFilter) -> Unit
) {
    val chips = listOfNotNull(
        filter.name?.let { FilterChipData("Name: $it") { onFilterRemoved(filter.copy(name = null)) } },
        filter.status?.let { FilterChipData("Status: $it") { onFilterRemoved(filter.copy(status = null)) } },
        filter.gender?.let { FilterChipData("Gender: $it") { onFilterRemoved(filter.copy(gender = null)) } },
        filter.species?.let { FilterChipData("Species: $it") { onFilterRemoved(filter.copy(species = null)) } },
    )

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(chips, key = { it.label }) { chip ->
            AssistChip(
                onClick = chip.onRemove,
                label = { Text(chip.label) },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remove Filter",
                        modifier = Modifier.size(16.dp)
                    )
                }
            )
        }
    }
}