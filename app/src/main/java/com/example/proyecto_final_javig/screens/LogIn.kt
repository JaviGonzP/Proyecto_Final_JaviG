package com.example.proyecto_final_javig.screens

import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.proyecto_final_javig.model.LoginScreenViewModel
import com.example.proyecto_final_javig.navigation.Screens
import com.example.proyecto_final_javig.ui.theme.blanco
import com.example.proyecto_final_javig.ui.theme.fondo_azul
import com.example.proyecto_final_javig.ui.theme.fondo_rosa
import com.example.proyecto_final_javig.ui.theme.gris
import com.gandiva.neumorphic.LightSource
import com.gandiva.neumorphic.neu
import com.gandiva.neumorphic.shape.Flat
import com.gandiva.neumorphic.shape.RoundedCorner

@Composable
fun LogIn(navController : NavController,
          viewModel: LoginScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()){
    var dialogLogin by remember { mutableStateOf(false) }
    var dialogCerrar by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()){
        Column(modifier = Modifier
            .fillMaxHeight()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        fondo_azul,
                        fondo_rosa
                    )
                )
            )
            .run {if (dialogLogin || dialogCerrar) blur(8.dp) else this}
        ) {

            IniSesion(
                navController,
                dialogLogin = {dialogLogin = it},
                dialogCerrar = {dialogCerrar = it}
                 )
        }
        if (dialogLogin){
            LoginDialog(onDismiss = { dialogLogin = false })
        }

        if (dialogCerrar){
            CerrarDialog(onDismiss = { dialogCerrar = false })
        }
    }
}


//Inicio de sesión
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IniSesion(navController : NavController,
          viewModel: LoginScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
              dialogLogin: (Boolean) -> Unit, dialogCerrar: (Boolean) -> Unit
) {
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    var PasswordVisible by remember { mutableStateOf(false) }

    val color1 = Color(0xFF7F3785)
    val color2 = Color(0xFF4B06BB)

    val activity = LocalContext.current as? Activity
    var showDialogCerrar by remember { mutableStateOf(false) }

    BackHandler {
        dialogCerrar(true)
    }

    Column(
        Modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        fondo_azul,
                        fondo_rosa
                    )
                )
            )
            .fillMaxWidth()
            .padding(bottom = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

    ){
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(top = 20.dp),
            verticalArrangement = Arrangement.Center
        ){
            Text(
                color = gris,
                text = "Inicio de sesión",
                style= TextStyle(fontSize = 40.sp)
            )
        }
        Spacer(modifier = Modifier.height(0.dp))

        Column ( modifier = Modifier
            .weight(3f),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Box(
                modifier = Modifier
                    .height(150.dp)
                    .width(150.dp)
                    .weight(2f)
            ){
                AsyncImage(model ="https://cdn-icons-png.freepik.com/512/4341/4341078.png?ga=GA1.1.2030702673.1717694703", contentDescription = null )
            }

            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                value = username.value,
                onValueChange = { username.value = it },
                label = {
                    Text("Usuario")
                },
                        modifier = Modifier.neu(
                        lightShadowColor = gris,
                darkShadowColor = blanco,
                shadowElevation = 4.dp,
                lightSource = LightSource.LEFT_BOTTOM,
                shape = Flat(RoundedCorner(12.dp)),
            )
            )
            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                value = password.value,
                onValueChange = { password.value = it },
                visualTransformation = if (PasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(
                        onClick = { PasswordVisible = !PasswordVisible }
                    ) {
                        val icon = if (PasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        Icon(icon, contentDescription = "Toggle password visibility")
                    }
                },
                label = {
                    Text("Contraseña")

                },
                modifier = Modifier.neu(
                    lightShadowColor = gris,
                    darkShadowColor = blanco,
                    shadowElevation = 4.dp,
                    lightSource = LightSource.LEFT_BOTTOM,
                    shape = Flat(RoundedCorner(12.dp)),
                )
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Column( modifier = Modifier
            .weight(1f).padding(top = 70.dp),
            verticalArrangement = Arrangement.Center
        ) {
                Button(
                    modifier = Modifier.height(50.dp).fillMaxWidth(.65f),
                    onClick = {
                        // Comprobar si los campos están vacíos
                        if (username.value.isNotEmpty() && password.value.isNotEmpty()) {
                            // Ir a la siguiente pantalla
                            Log.d("Lista Maestra", "Entrando con $username y $password")
                                viewModel.signInWithEmailAndPassword(username.value, password.value){ isAdmin ->
                                    navController.navigate(
                                        if(isAdmin) Screens.Admin.route else Screens.HomeContainer.route
                                    ) {
                                        popUpTo(Screens.LogIn.route) {
                                            inclusive = true
                                        }
                                    }
                                }
                        } else {
                            // Mostrar un diálogo de error
                           dialogLogin(true)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(color2),
                ) {
                    Text(text = "INICIAR SESIÓN", style = TextStyle(color = blanco, fontSize = 16.sp, fontWeight = Bold) )
                }
        }
        Spacer(modifier = Modifier.height(5.dp))
        ClickableText(
            style = TextStyle(
                textDecoration = TextDecoration.Underline,
                color = gris
            ),
            text = AnnotatedString( "Crea una cuenta"),
        onClick = {
            navController.navigate(Screens.SignUp.route)
        }
        )
        Spacer(modifier = Modifier.height(60.dp))
    }
}

@Composable
fun LoginDialog(onDismiss: () -> Unit){
    AlertDialog(onDismissRequest = { onDismiss() },
        confirmButton = { TextButton(onClick = { onDismiss() }) {
            Text(text = "Aceptar")
        }
        },
        title = { Text(text = "Error")},
        text = { Text(text = "Introduce un usuario y una contraseña.")})
}

@Composable
fun CerrarDialog(onDismiss: () -> Unit){
    val activity = LocalContext.current as? Activity
    AlertDialog(onDismissRequest = { onDismiss() },
        confirmButton = { TextButton(onClick = {  activity?.finish() }) {
            Text(text = "Si")
        }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("No")
            }
        },
        title = { Text(text = "Salir")},
        text = { Text(text = "¿Seguro que quieres cerrar la aplicación?")})
}