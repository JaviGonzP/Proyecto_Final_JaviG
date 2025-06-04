package com.example.proyecto_final_javig.screens

import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.proyecto_final_javig.model.LoginScreenViewModel
import com.example.proyecto_final_javig.navigation.Screens
import com.example.proyecto_final_javig.ui.theme.blanco
import com.example.proyecto_final_javig.ui.theme.colorAlpha1
import com.example.proyecto_final_javig.ui.theme.colorAlpha2
import com.example.proyecto_final_javig.ui.theme.colorBorde
import com.example.proyecto_final_javig.ui.theme.colorBoton
import com.example.proyecto_final_javig.ui.theme.colorFondo
import com.example.proyecto_final_javig.ui.theme.colorListaPrincipal
import com.example.proyecto_final_javig.ui.theme.colorTexto
import com.example.proyecto_final_javig.ui.theme.gris
import com.gandiva.neumorphic.LightSource
import com.gandiva.neumorphic.neu
import com.gandiva.neumorphic.shape.Flat
import com.gandiva.neumorphic.shape.RoundedCorner
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun LogIn(
    navController: NavController,
    viewModel: LoginScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var dialogLogin by remember { mutableStateOf(false) }
    var dialogCerrar by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxHeight().background(
                colorFondo.value,
            ).run { if (dialogLogin || dialogCerrar) blur(8.dp) else this }) {

            IniSesion(
                navController,
                dialogLogin = { dialogLogin = it },
                dialogCerrar = { dialogCerrar = it })
        }
        if (dialogLogin) {
            LoginDialog(onDismiss = { dialogLogin = false })
        }

        if (dialogCerrar) {
            CerrarDialog(onDismiss = { dialogCerrar = false })
        }
    }
}


//Inicio de sesión
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IniSesion(
    navController: NavController,
    viewModel: LoginScreenViewModel = viewModel(),
    dialogLogin: (Boolean) -> Unit,
    dialogCerrar: (Boolean) -> Unit
) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(color = colorFondo.value, darkIcons = true)

    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    var PasswordVisible by remember { mutableStateOf(false) }

    val activity = LocalContext.current as? Activity
    var showDialogCerrar by remember { mutableStateOf(false) }

    BackHandler {
        dialogCerrar(true)
    }

    Column(
        Modifier
            .background(
                colorFondo.value
            )
            .fillMaxWidth()
            .padding(bottom = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Column(
            modifier = Modifier
                .weight(1f),
//                .padding(top = 20.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                color = colorAlpha2.value.copy(alpha = 0.8f), text = "Inicio de sesión", style = TextStyle(fontSize = 40.sp)
            )
        }
        Spacer(modifier = Modifier.height(0.dp))

        Column(
            modifier = Modifier.weight(3f), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .height(150.dp)
                    .width(150.dp)
                    .weight(2f)
            ) {
                AsyncImage(
                    model = "https://cdn-icons-png.freepik.com/512/4341/4341078.png?ga=GA1.1.2030702673.1717694703",
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Correo electrónico",
                modifier = Modifier
                    .padding(bottom = 10.dp, start = 10.dp)
                    .fillMaxWidth(.65f),
                color = colorAlpha2.value.copy(0.8f),
                style = TextStyle(
                    fontSize = 16.sp, fontWeight = Bold
                ),
            )

            TextField(
                value = username.value,
                onValueChange = { username.value = it },
                placeholder = { "mail@example.com" },
                modifier = Modifier
                    .fillMaxWidth(.65f)
                    .clip(RoundedCornerShape(20.dp))
                    .border(2.dp, colorAlpha2.value.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                    .background(colorAlpha1.value.copy(alpha = 0.3f)),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = colorAlpha1.value.copy(alpha = 0.3f),
                    unfocusedIndicatorColor = Color.Transparent, // Removes blue underline
                    focusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Contraseña",
                modifier = Modifier
                    .padding(bottom = 10.dp, start = 10.dp)
                    .fillMaxWidth(.65f),
                color = colorAlpha2.value.copy(0.8f),
                style = TextStyle(
                    fontSize = 16.sp, fontWeight = Bold
                ),
            )

            TextField(
                value = password.value,
                onValueChange = { password.value = it },
                visualTransformation = if (PasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(
                        onClick = { PasswordVisible = !PasswordVisible }) {
                        val icon =
                            if (PasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        Icon(icon, contentDescription = "Toggle password visibility")
                    }
                },
                placeholder = { "mail@example.com" },
                modifier = Modifier
                    .fillMaxWidth(.65f)
                    .clip(RoundedCornerShape(20.dp))
                    .border(2.dp, colorAlpha2.value.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                    .background(colorAlpha1.value.copy(alpha = 0.3f)),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = colorAlpha1.value.copy(alpha = 0.3f),
                    unfocusedIndicatorColor = Color.Transparent, // Removes blue underline
                    focusedIndicatorColor = Color.Transparent
                )
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(top = 70.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth(.65f),
                onClick = {
                    // Comprobar si los campos están vacíos
                    if (username.value.isNotEmpty() && password.value.isNotEmpty()) {
                        // Ir a la siguiente pantalla
                        Log.d("Lista Maestra", "Entrando con $username y $password")
                        viewModel.signInWithEmailAndPassword(

                            username.value, password.value
                        ) { isAdmin ->
                            navController.navigate(
                                if (isAdmin) Screens.Admin.route else Screens.Principal.route
                            ) {
                                popUpTo(Screens.LogIn.route) {
                                    inclusive = true
                                }
                            }
                        }
                    } else {
//                         Mostrar un diálogo de error
                        dialogLogin(true)
                    }
                },
                colors = ButtonDefaults.buttonColors(colorBoton.value),
            ) {
                Text(
                    text = "INICIAR SESIÓN",
                    style = TextStyle(color = colorTexto.value, fontSize = 16.sp, fontWeight = Bold)
                )
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        ClickableText(
            style = TextStyle(
                fontWeight = Bold,
                textDecoration = TextDecoration.Underline, color = colorBoton.value
            ), text = AnnotatedString("Crea una cuenta"), onClick = {
                navController.navigate(Screens.SignUp.route)
            })
        Spacer(modifier = Modifier.height(60.dp))
    }
}

@Composable
fun LoginDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(text = "Aceptar")
            }
        },
        title = { Text(text = "Error") },
        text = { Text(text = "Introduce un usuario y una contraseña.") })
}

@Composable
fun CerrarDialog(onDismiss: () -> Unit) {
    val activity = LocalContext.current as? Activity
    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(onClick = { activity?.finish() }) {
                Text(text = "Si")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("No")
            }
        },
        title = { Text(text = "Salir") },
        text = { Text(text = "¿Seguro que quieres cerrar la aplicación?") })
}