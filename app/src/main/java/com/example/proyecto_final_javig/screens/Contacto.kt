package com.example.proyecto_final_javig.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyecto_final_javig.model.LoginScreenViewModel
import com.example.proyecto_final_javig.navigation.Screens
import com.example.proyecto_final_javig.ui.theme.blanco
import com.example.proyecto_final_javig.ui.theme.colorFondo
import com.example.proyecto_final_javig.ui.theme.gris
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Contacto(
    navController: NavController,
    viewModel: LoginScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    Barra4(navController)
    Column(
        modifier = Modifier
            .padding(top = 60.dp)
            .background(
                colorFondo.value
            )
    ) {
        Datos(navController)
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Datos(
    navController: NavController,
) {
    // Outer Column to arrange UI elements vertically
    Column(
        modifier = Modifier
            .padding(bottom = 100.dp)
            .fillMaxWidth(), // Background color
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        androidx.compose.material.Text(
            text = "Lista Maestra",
            style = TextStyle(
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .padding(16.dp)
        )

        Text(
            text = "Autor: Javi González",
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                fontSize = 24.sp
            ) // White text color
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Email: javigp127@gmail.com",
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                fontSize = 24.sp
            ) // White text color
        )
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Télefono: 722371490",
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                fontSize = 24.sp
            )
        )

        Spacer(modifier = Modifier.height(20.dp))
    }

    Spacer(modifier = Modifier.height(20.dp))

    Box(
        modifier = Modifier
            .padding(0.dp, 0.dp, 10.dp, 75.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Button(
            onClick = { navController.navigate(Screens.Mapa.route) },
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier
                .height(50.dp)
                .padding(bottom = 10.dp)
        ) {
            Text(text = "¿Dónde estamos?", color = Color.White)
        }
    }
}

@Composable
fun MapsScreen(navController: NavController) {
    val puçol = LatLng(39.614260, -0.313216)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(puçol, 10f)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 70.dp),
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = MarkerState(position = puçol),
                title = "Puçol",
                snippet = "Marcador en Puçol"
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Barra4(navController: NavController) {
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
                        text = "Contacto",
                        color = gris,
                        fontSize = 25.sp
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screens.Mapa.route) },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .height(50.dp)
            ) {
                Text(text = "Ubicación", color = Color.White)
            }
        }
    ) {

    }
}