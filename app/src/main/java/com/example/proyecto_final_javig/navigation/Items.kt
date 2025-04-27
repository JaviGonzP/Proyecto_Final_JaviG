package com.example.proyecto_final_javig.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.ui.graphics.vector.ImageVector

data class Item(
    val label: String,
    //val selected: Boolean,
    val icon: ImageVector,
    val contentDescription: String,
    val route: String
)

object Items{

    val items = arrayOf(
        Item("Principal", Icons.Default.Home, "Principal", Screens.Principal.route),
        Item("Ajustes", Icons.Default.AccountCircle, "Ajustes", Screens.Ajustes.route),
        Item("Scanner",  Icons.Default.QrCodeScanner, "Scanner", Screens.Scanner.route)
        //Item("Principal", false, Icons.Default.Home, "Principal", Screens.Principal.route)
    )
}
