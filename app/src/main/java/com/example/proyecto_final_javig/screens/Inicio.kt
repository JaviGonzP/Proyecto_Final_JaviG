package com.example.proyecto_final_javig.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Inicio(navController: NavController) {
    //Barra(navController)
}

/*data class Producto(
    val producto: String,
    val cantidad: String
)

val productos = mutableStateListOf<Producto>()*/


@RequiresApi(Build.VERSION_CODES.O)
fun getFecha(): String {
    val now = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH:mm:ss")
    return now.format(formatter)
}

@Composable
fun DialogVacio(
    vacio: Boolean,
    onDismiss: () -> Unit
) {
    if (vacio) {
        AlertDialog(onDismissRequest = { onDismiss() },
            confirmButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text(text = "Aceptar")
                }
            },
            title = { Text(text = "Error") },
            text = { Text(text = "Rellena los 2 campos") })
    }
}

@Composable
fun DialogLleno(
    lleno: Boolean,
    onDismiss: () -> Unit
) {
    if (lleno) {
        AlertDialog(onDismissRequest = { onDismiss() },
            confirmButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text(text = "Aceptar")
                }
            },
            title = { Text(text = "Error") },
            text = { Text(text = "Guarda la lista actual antes de crear una nueva") })
    }
}

@Composable
fun DialogLista(
    lista: Boolean,
    onDismiss: () -> Unit
) {
    if (lista) {
        AlertDialog(onDismissRequest = { onDismiss() },
            confirmButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text(text = "Aceptar")
                }
            },
            title = { Text(text = "Error") },
            text = { Text(text = "Tiene que haber una lista creada antes de añadirle productos") })
    }
}

/*@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Barra(navController: NavController) {
    Scaffold(
        topBar = {
            var showMenu by remember {
                mutableStateOf(false)
            }
            TopAppBar(
                //Colores
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = morado,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSecondary
                ),
                // Título de la barra superior
                title = {
                    Text(
                        text = "Crear listas",
                        color = Color.White,
                        fontSize = 25.sp
                    )
                }
            )
        },

        ) {
        Muestra()
    }
}*/



