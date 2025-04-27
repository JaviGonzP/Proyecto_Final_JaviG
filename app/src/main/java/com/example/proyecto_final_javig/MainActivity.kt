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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import com.example.proyecto_final_javig.navigation.ScreenNavInicio
import com.example.proyecto_final_javig.navigation.Screens
import com.example.proyecto_final_javig.navigation.SmoothAnimationBottomBarScreens
import com.example.proyecto_final_javig.ui.theme.Proyecto_Final_JaviGTheme

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Proyecto_Final_JaviGTheme {
                val navController = rememberNavController()

                //BottomBar ruta, texto e iconos
                val bottomNavigationItems = listOf(
                    SmoothAnimationBottomBarScreens(
                        Screens.Principal.route,
                        stringResource(id = R.string.inicio),
                        R.drawable.baseline_home_24
                    ),  SmoothAnimationBottomBarScreens(
                        //Screens.Ajustes.route ,
                        Screens.LogIn.route,
                        stringResource(id = R.string.perfil),
                        R.drawable.baseline_person_24
                    ), SmoothAnimationBottomBarScreens(
                        //Screens.Ajustes.route ,
                        Screens.Contacto.route,
                        stringResource(id = R.string.contacto),
                        R.drawable.baseline_info_24
                    )
                )

                //ScreenNavigationConfiguration(navController, currentIndex)
                val currentIndex = rememberSaveable {
                    mutableIntStateOf(0)
                }

                BlurEffectProvider {
                    Scaffold(
                    ) { innerPadding ->
                        Modifier.padding(innerPadding)
                        ScreenNavInicio(navController)
                        //LogIn(navController)
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
