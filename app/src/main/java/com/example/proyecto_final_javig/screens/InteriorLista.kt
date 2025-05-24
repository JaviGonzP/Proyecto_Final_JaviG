package com.example.proyecto_final_javig.screens

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyecto_final_javig.model.DataViewModel
import com.example.proyecto_final_javig.ui.theme.blanco
import com.example.proyecto_final_javig.ui.theme.fondo_azul
import com.example.proyecto_final_javig.ui.theme.fondo_rosa
import com.example.proyecto_final_javig.ui.theme.gris
import com.gandiva.neumorphic.LightSource
import com.gandiva.neumorphic.neu
import com.gandiva.neumorphic.shape.Flat
import com.gandiva.neumorphic.shape.RoundedCorner
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InteriorLista(
    navController: NavController,
    id: String,
    dataViewModel: DataViewModel = viewModel()
) {
    val getLis = dataViewModel.stateM.value
    val getProd = dataViewModel.stateP.value
    val selectedLista = getLis.find { it.id_lista == id }
    val selectedProd = getProd.find { it.id_lista == id }
    val firestore = FirebaseFirestore.getInstance()

    var dialogOpen by remember { mutableStateOf(false)}

    //Variables boton
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val elevation by animateDpAsState(if (isPressed) 2.dp else 4.dp)
    var showAddProductDialog by remember { mutableStateOf(false) }

    val interactionSourceComp = remember { MutableInteractionSource() }
    val isPressedComp by interactionSourceComp.collectIsPressedAsState()
    val elevationComp by animateDpAsState(if (isPressedComp) 2.dp else 4.dp)
    var showComp by remember { mutableStateOf(false) }

    if (selectedLista != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            fondo_azul,
                            fondo_rosa
                        )
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = selectedLista.nombre_lista,
                    fontSize = 25.sp,
                    color = gris,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.5f)
                ) {
                    IconButton(
                        onClick = { showComp = true },
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                Color.Gray,
                                CircleShape
                            )
                            .neu(
                                lightShadowColor = gris,
                                darkShadowColor = blanco,
                                shadowElevation = elevationComp,
                                lightSource = LightSource.LEFT_BOTTOM,
                                shape = Flat(RoundedCorner(26.dp)),
                            ),
                        interactionSource = interactionSourceComp,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Compartir",
                            tint = gris
                        )
                    }
                }
                IconButton(
                    onClick = { showAddProductDialog = true },
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .run {if (showComp || showAddProductDialog || dialogOpen) blur(8.dp) else this}
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    if (selectedProd != null) {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(getProd) { item ->
                                if (item.id_lista == id) {
                                    CardProd(
                                        nombre_producto = item.nombre_producto,
                                        id_lista = item.id_lista,
                                        navController = navController,
                                        setDialogOpen = { dialogOpen = it}
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    if (showAddProductDialog) {
        AddProductDialog(
            id_lista = id,
            onDismiss = { showAddProductDialog = false },
            onSave = { productName ->
                addProducto(firestore, id, productName)
                showAddProductDialog = false
            }
        )
    }
    if (showComp) {
        ShareDialog(
            firestore = firestore,
            id_lista = id,
            onDismiss = { showComp = false }
        )
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun ShareDialog(
    firestore: FirebaseFirestore,
    id_lista: String,
    onDismiss: () -> Unit
) {
    var id_compartir by remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect (id_lista) {
        firestore.collection("listas")
            .whereEqualTo("id_lista", id_lista)
            .get()
            .addOnSuccessListener { documents ->
            if(!documents.isEmpty){
                val document = documents.documents[0]
                id_compartir = document.getString("id_compartir") ?: "No disponible"
            } else {
                id_compartir = "No disponible"
            }
        }
    }
    AlertDialog (onDismissRequest = onDismiss,
        confirmButton = {
        TextButton(
            onClick = {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Código de lista: ", id_compartir)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(context, "Código copiado al portapapeles", Toast.LENGTH_SHORT).show()
                onDismiss()
        }) { Text("Copiar") }
    }, dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }, title = { Text("Compartir Lista") }, text = {
        Text(
            "Código de lista: $id_compartir"
        )
    } )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductDialog(
    id_lista: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var productName by remember { mutableStateOf("") }
    AlertDialog(onDismissRequest =
    onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onSave(
                    productName
                )
            }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        title = {
            Text("Añadir Producto")
        },
        text = {
            Column {
                OutlinedTextField(
                    value = productName,
                    onValueChange = { productName = it },
                    label = { Text("Nombre del Producto") }
                )
            }
        })
}

fun addProducto(firestore: FirebaseFirestore, id_lista: String, productName: String) {
    val producto = hashMapOf(
        "id_lista" to id_lista,
        "nombre_producto" to productName
    )
    firestore.collection("productos").add(producto)
        .addOnSuccessListener { Log.d("Success", "Producto añadido correctamente") }
        .addOnFailureListener { e -> Log.d("Error", "Error añadiendo producto: ", e) }
}