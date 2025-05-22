package com.example.proyecto_final_javig.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyecto_final_javig.model.DataViewModel
import com.example.proyecto_final_javig.model.UserViewModel
import com.example.proyecto_final_javig.ui.theme.blanco
import com.example.proyecto_final_javig.ui.theme.fondo_azul
import com.example.proyecto_final_javig.ui.theme.fondo_rosa
import com.example.proyecto_final_javig.ui.theme.gris
import com.gandiva.neumorphic.LightSource
import com.gandiva.neumorphic.neu
import com.gandiva.neumorphic.shape.Flat
import com.gandiva.neumorphic.shape.RoundedCorner
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.random.Random

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Principal(navController: NavController, userViewModel: UserViewModel = viewModel()) {
    var showDialog by remember { mutableStateOf(false) }


    Box(modifier = Modifier.fillMaxSize()){

        //Barra(navController, showDialog = {showDialog = it})
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
                .run {if (showDialog) blur(8.dp) else this}
        ) {

            Listas2(navController)
        }

        if (showDialog){
            AddListDialog(onDismiss = { showDialog = false })
        }
    }
}

data class Producto(
    val nombre_producto: String = "",
    val id_lista: String = "",
    val documentId: String = ""
)

val productos = mutableStateListOf<Producto>()


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Listas2(
    navController: NavController,
    dataViewModel: DataViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel()
) {
    //Variables
    var id_compartir_lis by remember { mutableStateOf("") }
    val items = dataViewModel.stateM.value //datos del viewmodel
    val users = userViewModel.stateU.value //datos del viewmodel

    val misListas = items.filter { it.id_user == users.user_id }
    val listasCompartidas = items.filter { users.id_compartir.any { id -> id == it.id_compartir } }

    var dialogOpen by remember { mutableStateOf(false) }

    val interactionSourceAny = remember { MutableInteractionSource() }
    val isPressedAny by interactionSourceAny.collectIsPressedAsState()
    val elevationAny by animateDpAsState(if (isPressedAny) 2.dp else 4.dp)

    // Estructura de la pantalla
    Box(
        modifier =
        Modifier
            .background(blanco)
            .shadow(
                25.dp,
                RectangleShape,
                clip = false
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .run {if (dialogOpen) blur(8.dp) else this}
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                fondo_azul,
                                fondo_rosa
                            )
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                ) {
                    Row() {
                        // Barra de búsqueda
                        OutlinedTextField(
                            value = id_compartir_lis,
                            onValueChange = { id_compartir_lis = it },
                            label = { Text("Añadir codigo de lista") },
                            //fontFamily = MonsFontBold,
                            modifier = Modifier
                                .width(275.dp)
                                .padding(start = 20.dp, end = 20.dp),
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.White
                            )
                        )
                        IconButton(
                            onClick = {
                                userViewModel.agregarIdCompartir(id_compartir_lis)
                                id_compartir_lis = ""
                            },
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .size(48.dp)
                                .background(
                                    Color.Gray,
                                    CircleShape
                                )
                                .neu(
                                    lightShadowColor = gris,
                                    darkShadowColor = blanco,
                                    shadowElevation = elevationAny,
                                    lightSource = LightSource.LEFT_BOTTOM,
                                    shape = Flat(RoundedCorner(26.dp)),
                                ),
                            interactionSource = interactionSourceAny,
                        ) {
                            Text(
                                text = "+", color = gris, style = TextStyle(fontSize = 35.sp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Muestra las listas propias o un mensaje si no hay resultados
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(Color.Transparent)
                        ) {
                            LazyColumn(
                                contentPadding = PaddingValues(2.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                items(misListas) { item ->
                                    CardLista(
                                        nombre_lista = item.nombre_lista,
                                        nombre_sitio = item.nombre_sitio,
                                        id_lista = item.id_lista,
                                        id_compartir = item.id_compartir,
                                        id_user = item.id_user,
                                        navController = navController,
                                        esCompartida = false,
                                        setDialogOpen = { dialogOpen = it }
                                    )
                                }
                                items(listasCompartidas) { item ->
                                    CardLista(
                                        nombre_lista = item.nombre_lista,
                                        nombre_sitio = item.nombre_sitio,
                                        id_lista = item.id_lista,
                                        id_compartir = item.id_compartir,
                                        id_user = item.id_user,
                                        navController = navController,
                                        esCompartida = true,
                                        setDialogOpen = { dialogOpen = it }
                                    )
                                }
                            }
                        }

                    }
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Barra(navController: NavController, showDialog: (Boolean) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val elevation by animateDpAsState(if (isPressed) 2.dp else 4.dp)

    Scaffold(topBar = {
        var showMenu by remember { mutableStateOf(false) }
        Box(
            modifier =
            Modifier
                .background(blanco)
                .shadow(
                    50.dp,
                    RectangleShape,
                    clip = false
                )
        ) {
            TopAppBar( //Colores
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = blanco,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSecondary
                ),
                // Título de la barra superior
                title = { Text(text = "Listas", color = gris, fontSize = 25.sp) },
                actions = {
                    IconButton(
                        onClick = { showDialog(true) },
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                Color.Gray,
                                CircleShape
                            )
                            .neu(
                                lightShadowColor = gris,
                                darkShadowColor = blanco,
                                shadowElevation = elevation,
                                lightSource = LightSource.LEFT_BOTTOM,
                                shape = Flat(RoundedCorner(26.dp)),
                            ),
                        interactionSource = interactionSource,
                    ) {
                        Text(
                            text = "+", color = gris, style = TextStyle(fontSize = 35.sp)
                        )
                    }
                }
            )
        }
    }) {

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddListDialog(onDismiss: () -> Unit,
                  userViewModel: UserViewModel = viewModel()) {
    var listName by remember { mutableStateOf("") }
    val firestore = FirebaseFirestore.getInstance()
    val userId = userViewModel.stateU.value.user_id
    AlertDialog(onDismissRequest =
    onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val idLista = generateRandomId()
                val idCompartir = generateRandomIdComp()
                saveListToFirebase(idLista, listName, userId, idCompartir, firestore)
                onDismiss()
            }) { Text("Aceptar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } },
        title = { Text("Nombre de la lista") },
        text = {
            OutlinedTextField(
                value = listName,
                onValueChange = { listName = it },
                label = { Text("Ingrese el nombre de la lista") })
        })
}

fun generateRandomId(): String {
    return Random.nextInt(100000000, 999999999).toString()
}

fun generateRandomIdComp(): String {
    return Random.nextInt(100000000, 999999999).toString()
}

fun saveListToFirebase(idLista: String, listName: String, userId: String, idCompartir: String, firestore: FirebaseFirestore) {
    val listData = hashMapOf(
        "id_lista" to idLista,
        "nombre_lista" to listName,
        "id_user" to userId,
        "id_compartir" to idCompartir
    )
    firestore.collection("listas").add(listData)
        .addOnSuccessListener { documentReference -> println("Lista añadida con ID: ${documentReference.id}") }
        .addOnFailureListener { e -> println("Error añadiendo lista: $e") }
}