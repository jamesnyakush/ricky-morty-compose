package com.jnyakush.rickymorty

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.jnyakush.core.Response
import com.jnyakush.rickymorty.ui.theme.RickyMortyTheme
import com.jnyakush.rickymorty.ui.viewmodel.CharacterViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RickyMortyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting()
                }
            }
        }
    }

}

@Composable
fun Greeting() {
    val vm = koinViewModel<CharacterViewModel>()

    LaunchedEffect(Unit) {
        vm.getCharacters()
    }


    when (val characterResponse = vm.characterResponse.value) {
        is Response.Loading -> {}
        is Response.Success -> {
            LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                characterResponse.data?.results?.let {
                    items(it){

                        Column(modifier = Modifier.padding(10.dp)) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(it.image)
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

                            Text(
                                modifier = Modifier.padding(0.dp, 14.dp),
                                text = it.name,
                                color = Color.Black,
                                fontSize = 18.sp,
                                fontStyle = FontStyle.Normal,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                modifier = Modifier.padding(0.dp, 14.dp),
                                text = it.status,
                                color = Color.Gray,
                                fontSize = 18.sp,
                                fontStyle = FontStyle.Normal,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        is Response.Failure -> {}
    }

}

