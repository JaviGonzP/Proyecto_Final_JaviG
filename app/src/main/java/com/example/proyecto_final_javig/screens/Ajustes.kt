package com.example.proyecto_final_javig.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyecto_final_javig.model.UserViewModel
import com.example.proyecto_final_javig.navigation.Screens
import com.example.proyecto_final_javig.ui.theme.AvatarResources
import com.example.proyecto_final_javig.ui.theme.blanco
import com.example.proyecto_final_javig.ui.theme.fondo_azul
import com.example.proyecto_final_javig.ui.theme.fondo_rosa
import com.example.proyecto_final_javig.ui.theme.gris
import com.gandiva.neumorphic.LightSource
import com.gandiva.neumorphic.neu
import com.gandiva.neumorphic.shape.RoundedCorner
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Ajustes(
    navController: NavController,
    userViewModel: UserViewModel = viewModel()
) {
    // 1) Leemos el usuario actual desde tu UserViewModel
    //    stateU.value es un User con: user_id, email, nombre, apellido, avatarUrl, etc.
    val usuario by userViewModel.stateU

    // 2) Estados locales para mostrar diálogos / editar campos
    var showEditDialog by remember { mutableStateOf(false) }
    var showAvatarDialog by remember { mutableStateOf(false) }
    var showLogoutConfirm by remember { mutableStateOf(false) }

    // Campos temporales para edición
    var newFirstName by remember { mutableStateOf(usuario.nombre) }
    var newLastName  by remember { mutableStateOf(usuario.apellido) }

    // La “clave” del avatar seleccionado actualmente
    var selectedAvatarKey by remember { mutableStateOf(usuario.avatarUrl) }

    // Si alguno de los tres diálogos está abierto, aplicamos blur a la parte inferior
    val shouldBlur = showEditDialog || showAvatarDialog || showLogoutConfirm

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(fondo_azul, fondo_rosa)
                )
            )
            .blur(if (shouldBlur) 8.dp else 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top= 80.dp, start = 16.dp, end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // ───────────────────────────────────────────────
            //  3) Foto de perfil circular dentro de una tarjeta
            Card(
                shape = RoundedCornerShape(100.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            ) {
                // Convertimos la “clave” avatarUrl en un R.drawable
                val avatarPair = AvatarResources.avatarList.firstOrNull { it.first == usuario.avatarUrl }
                val drawableId = avatarPair?.second ?: AvatarResources.avatarList.first().second

                Image(
                    painter = painterResource(id = drawableId),
                    contentDescription = "Avatar del usuario",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botón “Cambiar foto de perfil”
            IconButton(
                onClick = {
                    // Al abrir, inicializamos selectedAvatarKey en lo que ya tiene el usuario
                    selectedAvatarKey = usuario.avatarUrl
                    showAvatarDialog = true
                },
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.Gray, CircleShape)
                    .neu(
                        lightShadowColor = gris,
                        darkShadowColor = blanco,
                        shadowElevation = 4.dp,
                        lightSource = LightSource.LEFT_BOTTOM,
                        shape = com.gandiva.neumorphic.shape.Flat(RoundedCorner(20.dp))
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Photo,
                    contentDescription = "Cambiar avatar",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ───────────────────────────────────────────────
            //  4) Nombre completo y correo dentro de una tarjeta blanca
            Surface(
                tonalElevation = 4.dp,
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${usuario.nombre} ${usuario.apellido}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = usuario.email,
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ───────────────────────────────────────────────
            //  5) Botón “Editar nombre y apellido”
            IconButton(
                onClick = {
                    // Pre-llenamos los campos con los valores actuales
                    newFirstName = usuario.nombre
                    newLastName  = usuario.apellido
                    showEditDialog = true
                },
                modifier = Modifier
                    .size(50.dp)
                    .background(Color.Gray, CircleShape)
                    .neu(
                        lightShadowColor = gris,
                        darkShadowColor = blanco,
                        shadowElevation = 4.dp,
                        lightSource = LightSource.LEFT_BOTTOM,
                        shape = com.gandiva.neumorphic.shape.Flat(RoundedCorner(24.dp))
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar nombre",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ───────────────────────────────────────────────
            //  6) Botón “Cerrar sesión”
            TextButton(
                onClick = { showLogoutConfirm = true },
                colors = ButtonDefaults.textButtonColors(
                    containerColor = Color.Red
                ),
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(48.dp)
                    .clip(RoundedCornerShape(24.dp))
            ) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = "Cerrar sesión",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Cerrar sesión",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    // ────────────────────────────────────────────────────────────────────────────
    //  7) DIÁLOGO “Editar nombre y apellido”
    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Editar nombre y apellido") },
            text = {
                Column {
                    OutlinedTextField(
                        value = newFirstName,
                        onValueChange = { newFirstName = it },
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(containerColor = Color.White)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newLastName,
                        onValueChange = { newLastName = it },
                        label = { Text("Apellido") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(containerColor = Color.White)
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (newFirstName.isNotBlank() && newLastName.isNotBlank()) {
                        // Actualizamos Firestore: colección "users" donde = user_id
                        val db = FirebaseFirestore.getInstance()
                        db.collection("users")
                            .whereEqualTo("user_id", usuario.user_id)
                            .get()
                            .addOnSuccessListener { snapshot ->
                                if (!snapshot.isEmpty) {
                                    val docRef = snapshot.documents[0].reference
                                    docRef.update(
                                        mapOf(
                                            "nombre" to newFirstName,
                                            "apellido" to newLastName
                                        )
                                    )
                                    // <-- Ya no llamamos a userViewModel.refreshCurrentUser()
                                    showEditDialog = false
                                }
                            }
                            .addOnFailureListener { e ->
                                Log.e("Ajustes", "Error actualizando nombre: ${e.message}")
                            }
                    }
                }) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // ────────────────────────────────────────────────────────────────────────────
    //  8) DIÁLOGO “Cambiar avatar”
    if (showAvatarDialog) {
        AlertDialog(
            onDismissRequest = { showAvatarDialog = false },
            title = { Text("Selecciona un avatar") },
            text = {
                // Mostramos todos los avatares en filas de 2 en 2
                Column {
                    AvatarResources.avatarList.chunked(2).forEach { rowPair ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            rowPair.forEach { (key, resId) ->
                                Image(
                                    painter = painterResource(id = resId),
                                    contentDescription = "Avatar $key",
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(CircleShape)
                                        .clickable {
                                            // Al tocar un avatar, lo guardamos en Firestore
                                            val db = FirebaseFirestore.getInstance()
                                            db.collection("users")
                                                .whereEqualTo("user_id", usuario.user_id)
                                                .get()
                                                .addOnSuccessListener { snapshot ->
                                                    if (!snapshot.isEmpty) {
                                                        val docRef = snapshot.documents[0].reference
                                                        docRef.update("avatarUrl", key)
                                                            .addOnSuccessListener {
                                                                // <-- Ya no llamamos a userViewModel.refreshCurrentUser()
                                                                selectedAvatarKey = key
                                                                showAvatarDialog = false
                                                            }
                                                            .addOnFailureListener { e ->
                                                                Log.e("Ajustes", "Error actualizando avatar: ${e.message}")
                                                            }
                                                    }
                                                }
                                                .addOnFailureListener { e ->
                                                    Log.e("Ajustes", "Error buscando usuario: ${e.message}")
                                                }
                                        }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            },
            confirmButton = {
                // Podemos usar solo un botón “Cancelar” aquí, ya que la selección cierra el diálogo
                TextButton(onClick = { showAvatarDialog = false }) {
                    Text("Cancelar")
                }
            },
            dismissButton = {}
        )
    }

    // ────────────────────────────────────────────────────────────────────────────
    //  9) DIÁLOGO “Confirmar cerrar sesión”
    if (showLogoutConfirm) {
        AlertDialog(
            onDismissRequest = { showLogoutConfirm = false },
            title = { Text("Cerrar sesión") },
            text = { Text("¿Estás seguro de que quieres cerrar sesión?") },
            confirmButton = {
                TextButton(onClick = {
                    // Cerramos la sesión de Firebase Auth
                    FirebaseAuth.getInstance().signOut()
                    // Navegamos a LogIn y limpiamos el back stack
                    navController.navigate(Screens.LogIn.route) {
                        popUpTo(0)
                    }
                }) {
                    Text("Sí, cerrar sesión", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutConfirm = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
