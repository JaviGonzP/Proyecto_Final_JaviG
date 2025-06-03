package com.example.proyecto_final_javig.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.proyecto_final_javig.model.DataViewModel
import com.example.proyecto_final_javig.model.ListaItems
import com.example.proyecto_final_javig.model.ProductosItems
import com.example.proyecto_final_javig.model.UserViewModel
import com.example.proyecto_final_javig.model.deleteLista
import com.example.proyecto_final_javig.navigation.Screens
import com.example.proyecto_final_javig.properties.DrawerContent
import com.example.proyecto_final_javig.properties.TopBarCustom
import com.example.proyecto_final_javig.ui.theme.blanco
import com.example.proyecto_final_javig.ui.theme.fondo_azul
import com.example.proyecto_final_javig.ui.theme.fondo_rosa
import com.example.proyecto_final_javig.ui.theme.gris
import com.gandiva.neumorphic.LightSource
import com.gandiva.neumorphic.neu
import com.gandiva.neumorphic.shape.Flat
import com.gandiva.neumorphic.shape.RoundedCorner
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Principal(navController: NavHostController) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            //  ─────────── Aquí invocamos el DrawerContent que definimos ↑
            DrawerContent(
                navController = navController,
                drawerState = drawerState,
                scope = scope
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopBarCustom(
                    titulo = "Lista Maestra",
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                // ───────── Aquí va TODO el contenido original de Principal. ─────────
                PrincipalContent(navController)
            }
        }
    }
}

@Composable
private fun PrincipalContent(
    navController: NavHostController,
    userViewModel: UserViewModel = viewModel()
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var listaToDelete by remember { mutableStateOf<ListaItems?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .run { if (showAddDialog || showDeleteDialog) blur(8.dp) else this }
                .fillMaxHeight()
                .background(
                    brush = Brush.verticalGradient(listOf(fondo_azul, fondo_rosa))
                )
        ) {
            Button(
                onClick = { showAddDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "Crear lista",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Crear nueva lista")
            }

            Listas2(
                navController = navController,
                onRequestDelete = { lista ->
                    listaToDelete = lista
                    showDeleteDialog = true
                }
            )
        }

        // Diálogo de “añadir lista”
        if (showAddDialog) {
            AddListDialog(onDismiss = { showAddDialog = false })
        }

        // Diálogo de confirmación de borrado global
        if (showDeleteDialog && listaToDelete != null) {
            AlertDialog(
                onDismissRequest = {
                    showDeleteDialog = false
                    listaToDelete = null
                },
                title = { Text("¿Eliminar lista?") },
                text = { Text("Se eliminarán también sus productos. ¿Continuar?") },
                confirmButton = {
                    TextButton(onClick = {
                        deleteLista(listaToDelete!!.id_lista)
                        showDeleteDialog = false
                        listaToDelete = null
                    }) {
                        Text("Eliminar", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDeleteDialog = false
                        listaToDelete = null
                    }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

data class Producto(
    val nombre_producto: String = "",
    val id_lista: String = "",
    val documentId: String = "",
    var isChecked: Boolean = false
)

val productos = mutableStateListOf<Producto>()


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Listas2(
    navController: NavController,
    dataViewModel: DataViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel(),
    onRequestDelete: (ListaItems) -> Unit
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

    var showAlreadyOwnDialog by remember { mutableStateOf(false) }
    var dialogError by remember { mutableStateOf(false) }

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
                .run {if (dialogOpen || showAlreadyOwnDialog) blur(8.dp) else this}
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
                            modifier = Modifier
                                .width(275.dp)
                                .padding(start = 20.dp, end = 20.dp),
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.White
                            )
                        )
                        IconButton(
                            onClick = {
                                val ingresado = id_compartir_lis.trim()
                                // 1) Si está vacío, no hacemos nada
                                if (ingresado.isBlank()) {
                                    return@IconButton
                                }
                                // 2) Extraemos todos los id_compartir de las propias
                                val misIDcomp = misListas.map { it.id_compartir }
                                if (ingresado in misIDcomp) {
                                    // ... ya es una lista propia: mostramos un diálogo de aviso
                                    showAlreadyOwnDialog = true
                                    return@IconButton
                                }
                                // 3) Si no era propia, añadimos al array del usuario
                                userViewModel.agregarIdCompartir(ingresado)
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
                                contentPadding = PaddingValues(vertical = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxHeight()
                            ) {
                                // ——— Sección 1: MIS LISTAS ———
                                item {
                                    Text(
                                        text = "Mis listas",
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                                    )
                                }
                                if (misListas.isEmpty()) {
                                    item {
                                        Text(
                                            text = "No tienes listas propias",
                                            modifier = Modifier.padding(start = 16.dp)
                                        )
                                    }
                                } else {
                                    items(misListas, key = { it.id_lista }) { lista ->
                                        val productosEstaLista = dataViewModel.stateP.value
                                            .filter { it.id_lista == lista.id_lista }
                                        ListCard(
                                            lista = lista,
                                            productos = productosEstaLista,
                                            isShared = false,
                                            onEdit = {
                                                navController.navigate(Screens.InteriorLista.passId(lista.id_lista))
                                            },
                                            onDelete = { onRequestDelete(lista) },
                                            onToggleProduct = { producto ->
                                                toggleProductoComprado(producto)
                                            }
                                        )
                                    }
                                }

                                // ——— Sección 2: LISTAS COMPARTIDAS ———
                                item {
                                    Spacer(Modifier.height(16.dp))
                                    Text(
                                        text = "Listas compartidas",
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                                    )
                                }
                                if (listasCompartidas.isEmpty()) {
                                    item {
                                        Text(
                                            text = "No tienes listas compartidas",
                                            modifier = Modifier.padding(start = 16.dp)
                                        )
                                    }
                                } else {
                                    items(listasCompartidas, key = { it.id_lista }) { lista ->
                                        val productosEstaLista = dataViewModel.stateP.value
                                            .filter { it.id_lista == lista.id_lista }
                                        ListCard(
                                            lista = lista,
                                            productos = productosEstaLista,
                                            isShared = true,
                                            onEdit = {
                                                navController.navigate(Screens.InteriorLista.passId(lista.id_lista))
                                            },
                                            onDelete = { onRequestDelete(lista) },
                                            onToggleProduct = { producto ->
                                                toggleProductoComprado(producto)
                                            }
                                        )
                                    }
                                }
                            }
                            if (showAlreadyOwnDialog) {
                                AlertDialog(
                                    onDismissRequest = { showAlreadyOwnDialog = false },
                                    title = { Text("Lista ya existente") },
                                    text = { Text("No puedes añadir como compartida una lista que ya es tuya.") },
                                    confirmButton = {
                                        TextButton(onClick = { showAlreadyOwnDialog = false }) {
                                            Text("Aceptar")
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun toggleProductoComprado(producto: ProductosItems) {
    val nueva = !producto.comprado
    FirebaseFirestore.getInstance()
        .collection("productos")
        .document(producto.documentId)
        .update("comprado", nueva)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListCard(
    lista: ListaItems,
    productos: List<ProductosItems>,
    isShared: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggleProduct: (ProductosItems) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val bgColor = if (isShared) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface

    Surface(
        tonalElevation = 4.dp,
        shape = RoundedCornerShape(8.dp),
        color = bgColor,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { expanded = !expanded }
            .animateContentSize()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = lista.nombre_lista,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar lista")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Borrar lista", tint = Color.Red)
                }
            }

            AnimatedVisibility(expanded) {
                if (productos.isEmpty()) {
                    Text(text = "No hay productos en esta lista",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 8.dp),
                        fontSize = 16.sp)
                } else {
                    Column(modifier = Modifier.padding(top = 8.dp)) {
                        productos.forEach { producto ->
                            ProductRow(
                                producto = producto,
                                onToggle = { onToggleProduct(producto) }
                            )
                            Divider(modifier = Modifier.padding(vertical = 4.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductRow(
    producto: ProductosItems,
    onToggle: () -> Unit
) {
    var checked by remember { mutableStateOf(producto.comprado) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = {
                checked = it
                onToggle()
            }
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = producto.nombre_producto,
            style = MaterialTheme.typography.bodyLarge,
            textDecoration = if (checked) TextDecoration.LineThrough else TextDecoration.None
        )
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
