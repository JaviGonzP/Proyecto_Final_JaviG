package com.example.proyecto_final_javig.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyecto_final_javig.model.LoginScreenViewModel
import com.example.proyecto_final_javig.navigation.Screens
import com.example.proyecto_final_javig.ui.theme.blanco
import com.example.proyecto_final_javig.ui.theme.colorAlpha1
import com.example.proyecto_final_javig.ui.theme.colorAlpha2
import com.example.proyecto_final_javig.ui.theme.colorBoton
import com.example.proyecto_final_javig.ui.theme.colorFondo

@Composable
fun SignUp(
    navController: NavController,
    viewModel: LoginScreenViewModel = viewModel()
) {

    var dialogSignup by remember { mutableStateOf(false) }
    var dialogCoinciden by remember { mutableStateOf(false) }
    var dialogContra by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .background(
                    colorFondo.value,
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
    dialogSignup: (Boolean) -> Unit,
    dialogCoinciden: (Boolean) -> Unit,
    dialogContra: (Boolean) -> Unit
) {
    var nombre = remember { mutableStateOf("") }
    val apellido = remember { mutableStateOf("") }
    var mail = remember { mutableStateOf("") }
    var con = remember { mutableStateOf("") }
    var con2 = remember { mutableStateOf("") }
    var PasswordVisible1 by remember { mutableStateOf(false) }
    var PasswordVisible2 by remember { mutableStateOf(false) }

    var passwordError by remember { mutableStateOf<String?>(null) }
    var validacion by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf<String?>(null) }

    Column(
        Modifier
            .background(
                colorFondo.value,
            )
            .fillMaxWidth()
            .padding(bottom = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = Modifier
                .weight(.3f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            Text(
                color = colorAlpha2.value.copy(alpha = 0.8f),
                text = "Crear cuenta",
                style = TextStyle(fontSize = 40.sp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Nombre",
                modifier = Modifier
                    .padding(bottom = 10.dp, start = 10.dp)
                    .fillMaxWidth(.65f),
                color = colorAlpha2.value.copy(0.8f),
                style = TextStyle(
                    fontSize = 16.sp, fontWeight = Bold
                ),
            )

            TextField(
                value = nombre.value,
                onValueChange = { nombre.value = it },
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
                text = "Apellido",
                modifier = Modifier
                    .padding(bottom = 10.dp, start = 10.dp)
                    .fillMaxWidth(.65f),
                color = colorAlpha2.value.copy(0.8f),
                style = TextStyle(
                    fontSize = 16.sp, fontWeight = Bold
                ),
            )

            TextField(
                value = apellido.value,
                onValueChange = { apellido.value = it },
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
                text = "Email",
                modifier = Modifier
                    .padding(bottom = 10.dp, start = 10.dp)
                    .fillMaxWidth(.65f),
                color = colorAlpha2.value.copy(0.8f),
                style = TextStyle(
                    fontSize = 16.sp, fontWeight = Bold
                ),
            )

            TextField(
                value = mail.value,
                onValueChange = {
                    mail.value = it
                    //Comprobación en tienmpo real del mail
                    if (validacion) {
                        emailError = if (validarEmail(it)) null else "Formato de email inválido"
                    }
                },
                isError = validacion && emailError != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
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

            if (validacion && emailError != null) {
                Text(text = emailError!!, color = Color.Red)
            }


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
                value = con.value,
                onValueChange = {
                    con.value = it
                    passwordError = validarPassword(it)
                },
                visualTransformation = if (PasswordVisible1) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(
                        onClick = { PasswordVisible1 = !PasswordVisible1 }
                    ) {
                        val icon =
                            if (PasswordVisible1) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        Icon(
                            icon, contentDescription = "Toggle password visibility",
                            tint = colorAlpha2.value.copy(0.8f),
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = validacion && passwordError != null,
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
            if (validacion && passwordError != null) {
                Text(passwordError!!, color = Color.Red)
            }


            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Repetir contraseña",
                modifier = Modifier
                    .padding(bottom = 10.dp, start = 10.dp)
                    .fillMaxWidth(.65f),
                color = colorAlpha2.value.copy(0.8f),
                style = TextStyle(
                    fontSize = 16.sp, fontWeight = Bold
                ),
            )

            TextField(
                value = con2.value,
                onValueChange = { con2.value = it },
                visualTransformation = if (PasswordVisible2) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(
                        onClick = { PasswordVisible2 = !PasswordVisible2 }
                    ) {
                        val icon =
                            if (PasswordVisible2) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        Icon(
                            icon,
                            contentDescription = "Toggle password visibility",
                            tint = colorAlpha2.value.copy(0.8f),
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = validacion && con != con2,
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
            if (validacion && con.value != con2.value) {
                Text("Las contraseñas no coinciden", color = Color.Red)
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth(.65f),

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
                            navController.navigate(Screens.Principal.route) {
                                popUpTo(Screens.LogIn.route) {
                                    inclusive = true
                                }
                            }
                        }
                    } else if (nombre.value.isEmpty() || apellido.value.isEmpty()
                        || mail.value.isEmpty() || con.value.isEmpty()
                        || con2.value.isEmpty()
                    ) {
                        // Mostrar un diálogo de error
                        dialogSignup(true)
                    }
                },
                colors = ButtonDefaults.buttonColors(colorBoton.value),
            ) {
                Text(
                    text = "CREAR CUENTA",
                    style = TextStyle(color = blanco, fontSize = 16.sp, fontWeight = Bold)
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            //Texto Clickble para ir a inicio de sesión
            ClickableText(
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    fontWeight = Bold,
                    textDecoration = TextDecoration.Underline,
                    color = colorBoton.value,
                    textAlign = TextAlign.Center
                ),
                text = AnnotatedString("¿Ya tienes una cuenta?"),
                onClick = {
                    navController.navigate(Screens.LogIn.route) {
                        popUpTo(Screens.LogIn.route) { inclusive = true }
                    }
                }
            )
        }
    }
}

@Composable
fun SignupDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
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
    AlertDialog(
        onDismissRequest = { onDismiss() },
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