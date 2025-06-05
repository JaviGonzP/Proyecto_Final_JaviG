package com.example.proyecto_final_javig.screens

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyecto_final_javig.model.DataViewModel
import com.example.proyecto_final_javig.model.ProductosItems
import com.example.proyecto_final_javig.ui.theme.colorFondo
import com.example.proyecto_final_javig.ui.theme.blanco
import com.example.proyecto_final_javig.ui.theme.colorAlpha1
import com.example.proyecto_final_javig.ui.theme.colorAlpha2
import com.example.proyecto_final_javig.ui.theme.colorBoton
import com.example.proyecto_final_javig.ui.theme.colorTexto
import com.example.proyecto_final_javig.ui.theme.gris
import com.gandiva.neumorphic.LightSource
import com.gandiva.neumorphic.neu
import com.gandiva.neumorphic.shape.Flat
import com.gandiva.neumorphic.shape.RoundedCorner
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun InteriorLista(
    navController: NavController,
    id: String,
    dataViewModel: DataViewModel = viewModel()
) {

    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(
        color = colorFondo.value, darkIcons = true
    )

    // 1) Obtenemos la lista y sus productos desde el ViewModel
    val allLists = dataViewModel.stateM.value
    val allProducts = dataViewModel.stateP.value
    val selectedLista = allLists.find { it.id_lista == id }
    val productsOfThisList = allProducts.filter { it.id_lista == id }
    val firestore = FirebaseFirestore.getInstance()

    // 2) Estados para gestionar los diálogos y el blur:
    var showEditNameDialog by remember { mutableStateOf(false) }
    var showAddProductDialog by remember { mutableStateOf(false) }
    var showShareDialog by remember { mutableStateOf(false) }
    var showDeleteProductDialog by remember { mutableStateOf<Pair<Boolean, ProductosItems?>>(false to null) }
    var newListName by remember { mutableStateOf("") }
    var newProductName by remember { mutableStateOf("") }

    // 3) Estados para los botones (neumorfismo)
    val interactionSourceEdit = remember { MutableInteractionSource() }
    val isPressedEdit by interactionSourceEdit.collectIsPressedAsState()
    val elevationEdit by animateDpAsState(if (isPressedEdit) 2.dp else 4.dp)

    val interactionSourceAdd = remember { MutableInteractionSource() }
    val isPressedAdd by interactionSourceAdd.collectIsPressedAsState()
    val elevationAdd by animateDpAsState(if (isPressedAdd) 2.dp else 4.dp)

    val interactionSourceShare = remember { MutableInteractionSource() }
    val isPressedShare by interactionSourceShare.collectIsPressedAsState()
    val elevationShare by animateDpAsState(if (isPressedShare) 2.dp else 4.dp)

    if (selectedLista == null) {
        // Si no encontramos la lista, mostramos un mensaje genérico
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFEEEEEE)),
            contentAlignment = Alignment.Center
        ) {
            Text("Lista no encontrada", color = Color.Gray)
        }
        return
    }

    // 4) Composición principal
    Box(
        modifier = Modifier
            .fillMaxSize()
            // Aplicamos blur si cualquiera de los diálogos está abierto
            .blur(
                if (showEditNameDialog ||
                    showAddProductDialog ||
                    showShareDialog ||
                    showDeleteProductDialog.first
                ) 8.dp else 0.dp
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    colorFondo.value
                )
                .padding(bottom = 25.dp)
//                .padding(top = 80.dp)
        ) {
            // ─────────────────────────────────────────────────────
            //  CABECERA: Nombre de la lista + botones Edit, Share, Add
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = selectedLista.nombre_lista,
                    fontSize = 26.sp,
                    color = colorAlpha2.value.copy(alpha = 0.8f),
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 10.dp, start = 10.dp)
                )
                // Botón Editar nombre
                IconButton(
                    onClick = {
                        newListName = selectedLista.nombre_lista
                        showEditNameDialog = true
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .background(colorBoton.value, CircleShape),
                    interactionSource = interactionSourceEdit
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar nombre",
                        tint = colorTexto.value
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Botón Compartir
                IconButton(
                    onClick = { showShareDialog = true },
                    modifier = Modifier
                        .size(48.dp)
                        .background(colorBoton.value, CircleShape),
                    interactionSource = interactionSourceShare
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Compartir lista",
                        tint = colorTexto.value
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Botón Añadir producto
                IconButton(
                    onClick = {
                        newProductName = ""
                        showAddProductDialog = true
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .background(colorBoton.value, CircleShape),
                    interactionSource = interactionSourceAdd
                ) {
                    Text(text = "+", color = colorTexto.value, style = TextStyle(fontSize = 35.sp))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            //  LISTA DE PRODUCTOS (con tarjetas)
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (productsOfThisList.isEmpty()) {
                    item {
                        Text(
                            text = "No hay productos en esta lista, pulsa en el botón + para añadir uno",
                            color = colorAlpha2.value.copy(alpha = 0.8f),
                            modifier = Modifier.padding(top = 20.dp)
                        )
                    }
                } else {
                    items(productsOfThisList, key = { it.documentId }) { producto ->
                        ProductCardInterior(
                            producto = producto,
                            onToggle = {
                                // Cambia el campo “comprado” en Firestore
                                val nueva = !producto.comprado
                                if (producto.documentId.isNotBlank()) {
                                    FirebaseFirestore.getInstance()
                                        .collection("productos")
                                        .document(producto.documentId)
                                        .update("comprado", nueva)
                                        .addOnSuccessListener {
                                            producto.comprado = nueva
                                        }
                                        .addOnFailureListener { e ->
                                            Log.e(
                                                "InteriorLista",
                                                "Error toggling comprado: ${e.message}"
                                            )
                                        }
                                }
                            },
                            onDelete = {
                                // Abre diálogo de confirmación de borrado de producto
                                showDeleteProductDialog = true to producto
                            }
                        )
                    }
                }
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────
    //  5) DIALOGO “Editar nombre de lista”
    if (showEditNameDialog) {
        AlertDialog(
            onDismissRequest = { showEditNameDialog = false },
            title = { Text("Editar nombre de lista") },
            text = {
                OutlinedTextField(
                    value = newListName,
                    onValueChange = { newListName = it },
                    label = { Text("Nuevo nombre") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = androidx.compose.material3.TextFieldDefaults.textFieldColors(
                        textColor = colorAlpha2.value.copy(alpha = 0.8f),
                        containerColor = Color.White
                    )
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (newListName.isNotBlank()) {
                        // Actualizar Firestore: colección "listas" donde id_lista == id
                        val ref = FirebaseFirestore.getInstance().collection("listas")
                        ref.whereEqualTo("id_lista", id).get()
                            .addOnSuccessListener { snapshot ->
                                if (!snapshot.isEmpty) {
                                    val docRef = snapshot.documents[0].reference
                                    docRef.update("nombre_lista", newListName)
                                        .addOnSuccessListener {
                                            showEditNameDialog = false
                                        }
                                        .addOnFailureListener { e ->
                                            Log.e(
                                                "InteriorLista",
                                                "Error actualizando nombre: ${e.message}"
                                            )
                                        }
                                }
                            }
                            .addOnFailureListener { e ->
                                Log.e("InteriorLista", "Error buscando lista: ${e.message}")
                            }
                    }
                }) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditNameDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // ─────────────────────────────────────────────────────────────────
    //  6) DIALOGO “Añadir producto”
    if (showAddProductDialog) {
        AlertDialog(
            onDismissRequest = { showAddProductDialog = false },
            title = { Text("Añadir producto") },
            text = {
                OutlinedTextField(
                    value = newProductName,
                    onValueChange = { newProductName = it },
                    label = { Text("Nombre del producto") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = androidx.compose.material3.TextFieldDefaults.textFieldColors(
                        textColor = colorAlpha2.value.copy(alpha = 0.8f),
                        containerColor = Color.White
                    )
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (newProductName.isNotBlank()) {
                        // Insertar en Firestore
                        val nuevoProducto = hashMapOf(
                            "id_lista" to id,
                            "nombre_producto" to newProductName,
                            "comprado" to false
                        )
                        firestore.collection("productos")
                            .add(nuevoProducto)
                            .addOnSuccessListener {
                                showAddProductDialog = false
                            }
                            .addOnFailureListener { e ->
                                Log.e("InteriorLista", "Error añadiendo producto: ${e.message}")
                            }
                    }
                }) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddProductDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // ─────────────────────────────────────────────────────────────────
    //  7) DIALOGO “Compartir lista”
    if (showShareDialog) {
        ShareDialog(
            firestore = firestore,
            id_lista = id,
            onDismiss = { showShareDialog = false }
        )
    }

    // ─────────────────────────────────────────────────────────────────
    //  8) DIALOGO “Confirmar borrar producto”
    if (showDeleteProductDialog.first && showDeleteProductDialog.second != null) {
        val productoToDelete = showDeleteProductDialog.second!!
        AlertDialog(
            onDismissRequest = {
                showDeleteProductDialog = false to null
            },
            title = { Text("Eliminar producto") },
            text = { Text("¿Seguro que quieres eliminar “${productoToDelete.nombre_producto}”?") },
            confirmButton = {
                TextButton(onClick = {
                    // Borrar documento en Firestore
                    FirebaseFirestore.getInstance()
                        .collection("productos")
                        .document(productoToDelete.documentId)
                        .delete()
                        .addOnSuccessListener {
                            showDeleteProductDialog = false to null
                        }
                        .addOnFailureListener { e ->
                            Log.e("InteriorLista", "Error borrando producto: ${e.message}")
                        }
                }) {
                    Text("Eliminar", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteProductDialog = false to null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

// ──────────────────────────────────────────────────────────────────────────
//  Composable auxiliar para mostrar cada producto dentro de una tarjeta
@Composable
fun ProductCardInterior(
    producto: ProductosItems,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    var checked by remember { mutableStateOf(producto.comprado) }

    Surface(
        tonalElevation = 4.dp,
        shape = RoundedCornerShape(20.dp),
        color = colorAlpha1.value.copy(.3f),
        modifier = Modifier
            .fillMaxWidth()

            .clickable { /* opcional: expandir para más detalles */ }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = {
                    checked = it
                    onToggle()
                }
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = producto.nombre_producto,
                style = MaterialTheme.typography.bodyLarge,
                color = colorAlpha2.value.copy(alpha = 0.8f),
                textDecoration = if (checked) TextDecoration.LineThrough else TextDecoration.None,
                modifier = Modifier.weight(1f),
                fontSize = 18.sp
            )
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar producto",
                    tint = Color.Red
                )
            }
        }
    }
}

// ──────────────────────────────────────────────────────────────────────────
//  Diálogo para copiar el código “id_compartir” al portapapeles
@SuppressLint("SuspiciousIndentation")
@Composable
fun ShareDialog(
    firestore: FirebaseFirestore,
    id_lista: String,
    onDismiss: () -> Unit
) {
    var id_compartir by remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(id_lista) {
        firestore.collection("listas")
            .whereEqualTo("id_lista", id_lista)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val document = documents.documents[0]
                    id_compartir = document.getString("id_compartir") ?: "No disponible"
                } else {
                    id_compartir = "No disponible"
                }
            }
    }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Compartir Lista") },
        text = { Text("Código de lista: $id_compartir") },
        confirmButton = {
            TextButton(onClick = {
                val clipboard =
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Código de lista", id_compartir)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(context, "Código copiado al portapapeles", Toast.LENGTH_SHORT).show()
                onDismiss()
            }) {
                Text("Copiar")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Cancelar")
            }
        }
    )
}

// ──────────────────────────────────────────────────────────────────────────
//  Funciones para generar IDs aleatorios, si no las tienes ya definidas


// ──────────────────────────────────────────────────────────────────────────
//  Definimos este valor para no repetir “48.dp” en varios sitios
private val FortyEightDp = 48.dp
