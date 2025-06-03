package com.example.proyecto_final_javig

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.proyecto_final_javig.navigation.ScreenNavHost
import com.example.proyecto_final_javig.navigation.ScreenNavInicio
import com.example.proyecto_final_javig.ui.theme.Proyecto_Final_JaviGTheme

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Proyecto_Final_JaviGTheme {
                val navController = rememberNavController()

                BlurEffectProvider {
                    Scaffold(
                    ) { innerPadding ->
                        Modifier.padding(innerPadding)
                        ScreenNavHost(navController)
                    }
                }
            }
        }
    }
}

val LocalBlurEffect = compositionLocalOf { mutableStateOf(false) }

@Composable
fun BlurEffectProvider(content: @Composable () -> Unit) {
    val blurState = remember { mutableStateOf(false) }
    CompositionLocalProvider(LocalBlurEffect provides blurState) {
        content()
    }
}
