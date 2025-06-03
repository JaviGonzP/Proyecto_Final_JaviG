package com.example.proyecto_final_javig.screens

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
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
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
fun SignUp(
    navController: NavController,
    viewModel: LoginScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {

    var dialogSignup by remember { mutableStateOf(false) }
    var dialogCoinciden by remember { mutableStateOf(false) }
    var dialogContra by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            fondo_azul,
                            fondo_rosa
                        )
                    )
                )
                .run { if (dialogSignup || dialogCoinciden || dialogContra) blur(8.dp) else this }
        ) {
            CrearCuenta(
                navController,
                dialogSignup = { dialogSignup = it },
                dialogCoinciden = { dialogCoinciden = it },
                dialogContra = { dialogContra = it }
            )
        }
        if (dialogSignup) {
            SignupDialog(onDismiss = { dialogSignup = false })
        }

        if (dialogCoinciden) {
            CoincidenDialog(onDismiss = { dialogCoinciden = false })
        }
    }
}

//Crear una cuenta
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearCuenta(
    navController: NavController,
    viewModel: LoginScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    dialogSignup: (Boolean) -> Unit, dialogCoinciden: (Boolean) -> Unit, dialogContra: (Boolean) -> Unit
) {
    var nombre = remember { mutableStateOf("") }
    val apellido = remember { mutableStateOf("") }
    var mail = remember { mutableStateOf("") }
    var con = remember { mutableStateOf("") }
    var con2 = remember { mutableStateOf("") }
    var PasswordVisible1 by remember { mutableStateOf(false) }
    var PasswordVisible2 by remember { mutableStateOf(false) }

    var show by rememberSaveable { mutableStateOf(false) }
    var coinciden by rememberSaveable { mutableStateOf(false) }

    var passwordError by remember { mutableStateOf<String?>(null) }
    var validacion by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf<String?>(null) }


    val color1 = Color(0xFF7F3785)
    val color2 = Color(0xFF4B06BB)


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
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                color = gris,
                text = "Crear cuenta",
                style = TextStyle(fontSize = 40.sp)
            )
        }
        Column(
            modifier = Modifier
                .weight(3f),
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = nombre.value,
                onValueChange = { nombre.value = it },
                label = {
                    Text("Nombre")
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
                value = apellido.value,
                onValueChange = { apellido.value = it },
               label = {
                    Text("Apellido")
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
                value = mail.value,
                onValueChange = {
                    mail.value = it
                    //Comprobación en tienmpo real del mail
                    if (validacion) {
                        emailError = if (validarEmail(it)) null else "Formato de email inválido"
                    }
                },
               label = {
                    Text("Email")
                },
                isError = validacion && emailError != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.neu(
                    lightShadowColor = gris,
                    darkShadowColor = blanco,
                    shadowElevation = 4.dp,
                    lightSource = LightSource.LEFT_BOTTOM,
                    shape = Flat(RoundedCorner(12.dp)),
                )
            )
            if (validacion && emailError != null) {
                Text(emailError!!, color = Color.Red)
            }
            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                value = con.value,
                onValueChange = { con.value = it
                    passwordError = validarPassword(it)  },
                visualTransformation = if (PasswordVisible1) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(
                        onClick = { PasswordVisible1 = !PasswordVisible1 }
                    ) {
                        val icon = if (PasswordVisible1) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        Icon(icon, contentDescription = "Toggle password visibility")
                    }
                },
               label = {
                    Text("Contraseña")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError =  validacion && passwordError != null,
                modifier = Modifier.neu(
                    lightShadowColor = gris,
                    darkShadowColor = blanco,
                    shadowElevation = 4.dp,
                    lightSource = LightSource.LEFT_BOTTOM,
                    shape = Flat(RoundedCorner(12.dp)),
                )
            )
            if (validacion && passwordError != null) {
                Text(passwordError!!, color = Color.Red)
            }
            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                value = con2.value,
                onValueChange = { con2.value = it },
                visualTransformation = if (PasswordVisible2) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(
                        onClick = { PasswordVisible2 = !PasswordVisible2 }
                    ) {
                        val icon = if (PasswordVisible2) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        Icon(icon, contentDescription = "Toggle password visibility")
                    }
                },
                label = {
                    Text("Repite Contraseña")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = validacion && con != con2,
                modifier = Modifier.neu(
                    lightShadowColor = gris,
                    darkShadowColor = blanco,
                    shadowElevation = 4.dp,
                    lightSource = LightSource.LEFT_BOTTOM,
                    shape = Flat(RoundedCorner(12.dp)),
                )
            )
            if (validacion && con.value != con2.value) {
                Text("Las contraseñas no coinciden", color = Color.Red)
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row() {
                Button(
                    modifier = Modifier.height(50.dp).fillMaxWidth(.65f),

                    onClick = {
                        validacion = true
                        passwordError = validarPassword(con.value)
                        emailError = if (validarEmail(mail.value)) null else "Correo inválido"

                        // Comprobar si los campos están vacíos
                        if (nombre.value.isNotEmpty() && apellido.value.isNotEmpty()
                            && mail.value.isNotEmpty() && con.value.isNotEmpty()
                            && con2.value.isNotEmpty() && con.value == con2.value && passwordError == null
                            && emailError == null
                        ) {
                            // Ir a la siguiente pantalla
                            viewModel.createUserWithEmailAndPassword(
                                mail.value,
                                con.value,
                                nombre.value,
                                apellido.value
                            ) {
                                navController.navigate(Screens.HomeContainer.route) {
                                    popUpTo(Screens.LogIn.route) {
                                        inclusive = true
                                    }
                                }
                            }
                        } else if(nombre.value.isEmpty() || apellido.value.isEmpty()
                            || mail.value.isEmpty() || con.value.isEmpty()
                            || con2.value.isEmpty()){
                            // Mostrar un diálogo de error
                            dialogSignup(true)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(color2),
                ) {
                    Text(text = "CREAR CUENTA", style = TextStyle(color = blanco, fontSize = 16.sp, fontWeight = Bold) )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            //Texto Clickble para ir a inicio de sesión
            ClickableText(
                style = TextStyle(
                    textDecoration = TextDecoration.Underline,
                    color = gris
                ),
                text = AnnotatedString("¿Ya tienes una cuenta?"),
                onClick = {
                    navController.navigate(Screens.LogIn.route){
                        popUpTo(Screens.LogIn.route) { inclusive = true }
                    }
                }
            )
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

@Composable
fun SignupDialog(onDismiss: () -> Unit) {
    AlertDialog(onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(text = "Aceptar")
            }
        },
        title = { Text(text = "Error") },
        text = { Text(text = "Rellena todos los campos.") })

}

@Composable
fun CoincidenDialog(onDismiss: () -> Unit) {
        AlertDialog(onDismissRequest = { onDismiss() },
            confirmButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text(text = "Aceptar")
                }
            },
            title = { Text(text = "Error") },
            text = { Text(text = "Las contraseñas no coincden") })
}

fun validarPassword(password: String): String? {
    val regex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,16}$")

    return when {
        password.length < 8 || password.length > 16 -> "Debe tener entre 8 y 16 caracteres."
        !password.contains(Regex("[a-z]")) -> "Debe incluir al menos una minúscula."
        !password.contains(Regex("[A-Z]")) -> "Debe incluir al menos una mayúscula."
        !password.contains(Regex("\\d")) -> "Debe incluir al menos un número."
        !password.contains(Regex("[@#$%^&+=!]")) -> "Debe incluir al menos un símbolo\n especial (@#\$%^&+=!)."
        !password.matches(regex) -> "La contraseña no cumple con los requisitos."
        else -> null // Contraseña válida
    }
}

// Función para validar el formato de email
fun validarEmail(email: String): Boolean {
    val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-zA-Z]{2,6}$")
    return emailRegex.matches(email)
}