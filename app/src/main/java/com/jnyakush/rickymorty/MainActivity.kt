package com.jnyakush.rickymorty

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.compose.rememberNavController
import com.jnyakush.rickymorty.ui.navigation.AppNavHost
import com.jnyakush.rickymorty.ui.theme.RickyMortyTheme
import com.jnyakush.rickymorty.util.RequestNotificationPermission
import timber.log.Timber

class MainActivity : ComponentActivity() {

    @SuppressLint("TimberArgCount")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RickyMortyTheme {
                val navController = rememberNavController()

                RequestNotificationPermission(
                    onPermissionGranted = {
                        Timber.Forest.d("Permission", "Granted")
                    },
                    onPermissionDenied = {
                        Timber.Forest.d("Permission", "Denied")
                    }
                )

                AppNavHost(navController)
            }
        }
    }
}