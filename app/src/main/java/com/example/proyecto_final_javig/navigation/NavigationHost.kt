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
import com.example.proyecto_final_javig.screens.Admin
import com.example.proyecto_final_javig.screens.Ajustes
import com.example.proyecto_final_javig.screens.InteriorLista
import com.example.proyecto_final_javig.screens.LogIn
import com.example.proyecto_final_javig.screens.Principal
import com.example.proyecto_final_javig.screens.Scanner
import com.example.proyecto_final_javig.screens.SignUp
import com.example.proyecto_final_javig.screens.UsersAdmin

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

        composable(Screens.Admin.route) {
            Admin(navController)
        }
        composable("usersAdmin/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            UsersAdmin(userId)
        }
    }
}

@Composable
@RequiresApi(Build.VERSION_CODES.O)
fun ScreenNavHost(navController: NavHostController) {

    NavHost(navController = navController, startDestination = Screens.LogIn.route) {

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

        composable(Screens.Scanner.route) {
            Scanner(navController)
        }

        composable(
            route = Screens.InteriorLista.route,
            arguments = listOf(navArgument(DETAIL_ARGUMENT_KEY) {
                type = NavType.StringType
            })
        )
        {
            val id = it.arguments?.getString(DETAIL_ARGUMENT_KEY).toString()
            Log.d("Args", it.arguments?.getString(DETAIL_ARGUMENT_KEY).toString())
            InteriorLista(navController, id)
        }
        composable(Screens.Admin.route) {
            Admin(navController)
        }
        composable("usersAdmin/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            UsersAdmin(userId)
        }
    }
}