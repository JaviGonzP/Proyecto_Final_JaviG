package com.example.proyecto_final_javig.screens

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PeopleAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyecto_final_javig.model.deleteLista
import com.example.proyecto_final_javig.model.deleteProducto
import com.example.proyecto_final_javig.navigation.Screens
import com.example.proyecto_final_javig.ui.theme.azul
import com.example.proyecto_final_javig.ui.theme.texto_negro
import com.gandiva.neumorphic.LightSource
import com.gandiva.neumorphic.neu
import com.gandiva.neumorphic.shape.Flat
import com.gandiva.neumorphic.shape.RoundedCorner
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CardLista(
    nombre_lista: String,
    nombre_sitio: String,
    id_lista: String,
    id_compartir: String,
    id_user: String,
    navController: NavController,
    modifier: Modifier = Modifier,
    esCompartida: Boolean,
    setDialogOpen: (Boolean) -> Unit
) {
    // Dimensiones de la tarjeta
    val cardHeight = 60
    val cardWidth = 150
    val interactionSource = remember { MutableInteractionSource() }
    val isPressedCard by interactionSource.collectIsPressedAsState()
    val elevationCard by animateDpAsState(if (isPressedCard) 4.dp else 10.dp)

    //Para el dialog
    var showDialog by remember { mutableStateOf(false) }
    if (showDialog) {
        AlertDialog(onDismissRequest = { showDialog = false; setDialogOpen(false) },
            confirmButton = {
                TextButton(onClick = {
                    deleteLista(id_lista)
                    showDialog = false
                    setDialogOpen(false)
                }) {
                    androidx.compose.material3.Text("Borrar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false; setDialogOpen(false) }) {
                    androidx.compose.material3.Text("Cancelar")
                }
            },
            title = { androidx.compose.material3.Text("Confirmar") },
            text = { androidx.compose.material3.Text("¿Está seguro de que quiere eliminar la lista?") }
        )
    }

    // Tarjeta con borde redondeado y sombra
    Card(
        modifier = Modifier
            .fillMaxWidth() // Ocupa el ancho máximo disponible
            .padding(2.dp) // Ajuste el padding según sea necesario
            .width(cardWidth.dp) // Ancho fijo
            .height(cardHeight.dp)
            .neu(
                lightShadowColor = Color.White,
                darkShadowColor = Color.Gray,
                shadowElevation = elevationCard,
                lightSource = LightSource.LEFT_TOP,
                shape = Flat(RoundedCorner(16.dp))
            ),
        shape = RoundedCornerShape(16.dp),
        onClick = {
            navController.navigate(route = Screens.InteriorLista.passId(id_lista))
            Log.d(
                "BasicItemCard",
                "Clicked card with ID: ${id_lista}"
            )
        }
    ) {
        // Fila que organiza la imagen y el texto horizontalmente
        Row(
            modifier = Modifier
                .background(azul)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    // nombre de la lista
                    Text(
                        modifier = Modifier
                            .padding(start = 16.dp),
                        text = nombre_lista,
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold,
                        //fontFamily = MonsFontBold,
                        color = texto_negro
                    )
                }
                if (esCompartida) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        modifier = Modifier.padding(top = 6.dp),
                        imageVector = Icons.Default.PeopleAlt,
                        contentDescription = "Compartida",
                        tint = texto_negro
                    )
                }
                IconButton(
                    onClick = {
                        showDialog = true
                        setDialogOpen(true)
                    },
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = Color.Red
                    )
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CardProd(
    nombre_producto: String,
    id_lista: String,
    navController: NavController,
    setDialogOpen: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    // Dimensiones de la tarjeta
    val cardHeight = 60
    val cardWidth = 100
    val interactionSource = remember { MutableInteractionSource() }
    val isPressedCard by interactionSource.collectIsPressedAsState()
    val elevationCard by animateDpAsState(if (isPressedCard) 4.dp else 10.dp)

    //Para el dialog
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(onDismissRequest = { showDeleteDialog = false; setDialogOpen(false) },
            confirmButton = {
                TextButton(onClick = {
                    deleteProducto(nombre_producto)
                    showDeleteDialog = false
                    setDialogOpen(false)
                }) {
                    androidx.compose.material3.Text("Borrar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false; setDialogOpen(false) }) {
                    androidx.compose.material3.Text("Cancelar")
                }
            },
            title = { androidx.compose.material3.Text("Confirmar") },
            text = { androidx.compose.material3.Text("¿Está seguro de que quiere eliminar el producto?") }
        )
    }

    if (showEditDialog) {
        EditProductDialog(
            nombre_producto = nombre_producto,
            id_lista = id_lista,
            onDismiss = { showEditDialog = false; setDialogOpen(false) },
            onSave = { newName ->
                updateProductName(id_lista, nombre_producto, newName)
                showEditDialog = false
                setDialogOpen(false)
            })
    }

    // Tarjeta con borde redondeado y sombra
    Card(
        modifier = modifier
            .fillMaxWidth() // Ocupa el ancho máximo disponible
            .padding(4.dp) // Ajuste el padding según sea necesario
            .width(cardWidth.dp) // Ancho fijo
            .height(cardHeight.dp)
            .neu(
                lightShadowColor = Color.White,
                darkShadowColor = Color.Gray,
                shadowElevation = elevationCard,
                lightSource = LightSource.LEFT_TOP,
                shape = Flat(RoundedCorner(26.dp))
            ),
        shape = RoundedCornerShape(16.dp), // Bordes redondeados
        elevation = 6.dp, // Sombra
        onClick = {
            navController.navigate(route = Screens.InteriorLista.passId(id_lista))
            Log.d(
                "BasicItemCard",
                "Clicked card with ID: ${id_lista}"
            )
        }
    ) {
        // Fila que organiza la imagen y el texto horizontalmente
        Row(
            modifier = Modifier
                .background(azul)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
            ) {
                // Nombre del producto
                Text(
                    modifier = Modifier
                        .padding(start = 16.dp, top = 8.dp),
                    text = nombre_producto,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = texto_negro
                )
                Spacer(modifier = Modifier.weight(0.1f))

                // Icono Editar
                IconButton(
                    onClick = {
                        showEditDialog = true
                        setDialogOpen(true)
                    },
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Create,
                        //painter = painterResource(id = R.drawable.ojo),
                        contentDescription = "Editar",
                        tint = texto_negro
                    )
                }
                // Icono borrar
                IconButton(
                    onClick = {
                        showDeleteDialog = true
                        setDialogOpen(true)
                    },
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        //painter = painterResource(id = R.drawable.ojo),
                        contentDescription = "Eliminar",
                        tint = Color.Red
                    )
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductDialog(
    nombre_producto: String,
    id_lista: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var newName by remember { mutableStateOf(nombre_producto) }

    AlertDialog(onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onSave(newName)
                }) {
                Text("Guardar")
            }
        },
        title = {
            Text("Editar Producto")
        },
        text = {
            OutlinedTextField(
                value = newName,
                onValueChange = { newName = it },
                label = { Text("Nombre del Producto") }
            )
        }
    )
}

fun updateProductName(
    id_lista: String,
    oldName: String,
    newName: String,
) {
    FirebaseFirestore.getInstance().collection("productos")
        .whereEqualTo("id_lista", id_lista)
        .whereEqualTo("nombre_producto", oldName)
        .get()
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    document.reference.update("nombre_producto", newName)
                        .addOnSuccessListener {
                            Log.d("Success", "Nombre de producto actualizado correctamente")
                        }
                        .addOnFailureListener { e ->
                            Log.d("Error", "Error actualizando nombre de producto: ", e)
                        }
                }
            } else {
                Log.d("Error", "Error getting documents: ", task.exception)
            }
        }
}


