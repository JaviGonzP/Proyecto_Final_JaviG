package com.example.proyecto_final_javig.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.proyecto_final_javig.model.UserViewModel
import com.example.proyecto_final_javig.model.ViewModel
import com.example.proyecto_final_javig.navigation.Screens
import com.example.proyecto_final_javig.ui.theme.blanco
import com.example.proyecto_final_javig.ui.theme.fondo_azul
import com.example.proyecto_final_javig.ui.theme.fondo_rosa
import com.example.proyecto_final_javig.ui.theme.gris

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Ajustes(navController: NavController) {
    Barra2(navController)
    Column(
        modifier = Modifier
            .padding(top = 60.dp)
            .fillMaxHeight()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        fondo_azul,
                        fondo_rosa
                    )
                )
            )
    ) {
        Opciones(navController)
    }
}

@Composable
fun Opciones(
    navController: NavController, viewModel: ViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel()
) {
    val getUser = userViewModel.stateU.value


    Column(
        modifier = Modifier
            .fillMaxSize(),// Background color
        verticalArrangement = Arrangement.Top, // Align elements to the top
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(model = "https://cdn.icon-icons.com/icons2/3946/PNG/512/user_icon_250929.png",
            contentDescription = null,
            contentScale = ContentScale.Crop, // Ajusta la imagen al tamaño del contenedor
            modifier = Modifier
                .padding(top = 64.dp)
                .clip(CircleShape)
                .size(128.dp) // Altura de la imagen
        )
        Spacer(modifier = Modifier.height(20.dp))
        // Second text
        Text(
            text = "Usuario: " + getUser.nombre + " " + getUser.apellido,
            style = TextStyle(
                fontSize = 18.sp
            )
        )
        Text(
            text = "Correo: " + getUser.email,
            style = TextStyle(
                fontSize = 18.sp
            )
        )

    }
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Button(
            onClick = { navController.navigate(Screens.SignUp.route) },
            modifier = Modifier
                .padding(5.dp),
            colors = ButtonDefaults.buttonColors(blanco)
        ) {
            Text(
                text = "Crear nueva cuenta"
            )
        }
        Button(
            onClick = {
                navController.navigate(Screens.LogIn.route){
                    popUpTo(Screens.LogIn.route) { inclusive = true }
                }
            },
            modifier = Modifier
                .padding(5.dp),
            colors = ButtonDefaults.buttonColors(blanco)
        ) {
            Text(
                color = Color.Red,
                text = "Cerrar sesion"
            )
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Barra2(navController: NavController) {
    Scaffold(
        topBar = {
            var showMenu by remember {
                mutableStateOf(false)
            }
            TopAppBar(
                //Colores
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = blanco,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSecondary
                ),
                // Título de la barra superior
                title = {
                    Text(
                        text = "Perfil",
                        color = gris,
                        fontSize = 25.sp
                    )
                },
                actions = {
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = null,
                            tint = gris

                        )
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        modifier = Modifier.width(180.dp)
                    ) {
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.ExitToApp,
                                    contentDescription = null
                                )
                            },
                            text = { Text(text = "Cerrar sesión") },
                            onClick = { navController.navigate(Screens.LogIn.route) })
                    }
                }
            )
        }

    ) {

    }
}

