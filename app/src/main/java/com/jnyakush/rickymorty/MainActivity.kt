package com.jnyakush.rickymorty


import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.jnyakush.rickymorty.data.model.Character
import com.jnyakush.rickymorty.ui.theme.RickyMortyTheme
import com.jnyakush.rickymorty.ui.viewmodel.CharacterFilter
import com.jnyakush.rickymorty.ui.viewmodel.CharacterViewModel
import com.jnyakush.rickymorty.util.Response
import org.koin.androidx.compose.koinViewModel
import androidx.compose.foundation.lazy.items

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissions()

        setContent {
            RickyMortyTheme {
                val navController = rememberNavController()

                AppNavHost(navController)
            }
        }
    }

    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(
                        android.Manifest.permission.POST_NOTIFICATIONS
                    ), 12345
                )
            }
        }
    }
}

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailScreen(
    characterId: Int,
    navController: NavHostController,
    viewModel: CharacterViewModel = koinViewModel()
) {
    val characterResponse by viewModel.characterById.collectAsState()

    LaunchedEffect(characterId) {
        viewModel.getCharacterById(characterId)
    }

    val characterName = (characterResponse as? Response.Success)?.data?.name ?: "Character Details"

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = characterName,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        content = { padding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                color = MaterialTheme.colorScheme.background
            ) {
                when (val result = characterResponse) {
                    is Response.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is Response.Failure -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Failed to load character",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    is Response.Success -> {
                        val character = result.data
                        if (character == null) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "Character not found",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        } else {
                            CharacterDetailContent(character)

                        }
                    }
                }
            }
        }
    )
}

@Composable
fun CharacterDetailContent(
    character: Character
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(character.image)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.ic_launcher_background),
            contentDescription = character.name,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(16.dp)
                .clip(RoundedCornerShape(10))
        )

        Card(
            modifier = Modifier.padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFFEFE),
            ),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                InfoRow(
                    label = "Status",
                    value = character.status
                )

                InfoRow(
                    label = "Species",
                    value = character.species
                )

                InfoRow(
                    label = "Type",
                    value = if (character.type.isNotBlank()) character.type else "Unknown"
                )

                InfoRow(
                    label = "Gender",
                    value = character.gender
                )
                InfoRow(
                    label = "Origin",
                    value = character.origin.name
                )

                InfoRow(
                    label = "Location",
                    value = character.location.name
                )

                InfoRow(
                    label = "Episodes",
                    value = "${character.episode.size} appearances"
                )

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun InfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen(
    navController: NavHostController,
    viewModel: CharacterViewModel = koinViewModel()
) {
    val characters = viewModel.characters.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Characters",
                        fontWeight = FontWeight.Bold
                    )
                },
            )
        },
        content = { padding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    CharacterSearchBar(viewModel)

                    when (val state = characters.loadState.refresh) {
                        is LoadState.Loading -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        is LoadState.Error -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Error loading characters: ${state.error.message}")
                            }
                        }

                        else -> {
                            CharacterContent(characters, navController)
                        }
                    }
                }
            }
        }
    )

}

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


data class FilterChipData(val label: String, val onRemove: () -> Unit)

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

@Composable
fun CharacterCard(
    character: Character,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(12.dp))
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(character.image)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.ic_launcher_background),
                contentDescription = stringResource(R.string.app_name),
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .height(180.dp)
                    .clip(
                        RoundedCornerShape(10)
                    )
            )

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .background(
                        color = if (character.status == "Alive") {
                            Color(0xFF4CAF50)
                        } else if (character.status == "Dead") {
                            Color.Red
                        } else {
                            Color.Gray
                        },
                        shape = RoundedCornerShape(25)
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = character.status,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = character.name,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        Text(
            text = character.species,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}

sealed class Screen(val route: String) {
    object CharacterList : Screen("character_list")
    object CharacterDetail : Screen("character_detail/{characterId}") {
        fun createRoute(characterId: Int) = "character_detail/$characterId"
    }
}