package com.example.proyecto_final_javig.navigation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.proyecto_final_javig.properties.NavDrawer
import com.example.proyecto_final_javig.screens.Ajustes
import com.example.proyecto_final_javig.screens.Contacto
import com.example.proyecto_final_javig.screens.InteriorLista
import com.example.proyecto_final_javig.screens.LogIn
import com.example.proyecto_final_javig.screens.MapsScreen
import com.example.proyecto_final_javig.screens.Principal
import com.example.proyecto_final_javig.screens.Scanner
import com.example.proyecto_final_javig.screens.SignUp

@Composable
@RequiresApi(Build.VERSION_CODES.O)
fun ScreenNavInicio(navController: NavHostController) {

    NavHost(navController = navController, startDestination = Screens.LogIn.route) {

        composable(Screens.HomeContainer.route) {
            NavDrawer(navController)
        }

        composable(Screens.LogIn.route) {
            LogIn(navController)
        }

        composable(Screens.SignUp.route) {
            SignUp(navController)
        }

    }
}

@Composable
@RequiresApi(Build.VERSION_CODES.O)
fun ScreenNavHost(navController: NavHostController) {

    NavHost(navController = navController, startDestination = Screens.Principal.route) {

        composable(Screens.HomeContainer.route) {
            NavDrawer(navController)
        }

        composable(Screens.Principal.route) {
            Principal(navController)
        }
        composable(Screens.Ajustes.route) {
            Ajustes(navController)
        }

        composable(Screens.LogIn.route) {
            LogIn(navController)
        }

        composable(Screens.SignUp.route) {
            SignUp(navController)
        }
        composable(Screens.Contacto.route) {
            Contacto(navController)
        }
        composable(Screens.Scanner.route) {
            Scanner()
        }
        composable(Screens.Mapa.route) {
            MapsScreen(navController = navController)
        }

        composable(route = Screens.InteriorLista.route,
            arguments = listOf(navArgument(DETAIL_ARGUMENT_KEY) {
                type = NavType.StringType
            })
        )
        {
            val id = it.arguments?.getString(DETAIL_ARGUMENT_KEY).toString()
            Log.d("Args", it.arguments?.getString(DETAIL_ARGUMENT_KEY).toString())
            InteriorLista(navController, id)
        }
    }
}