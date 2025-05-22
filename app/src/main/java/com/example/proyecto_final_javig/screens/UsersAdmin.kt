package com.example.proyecto_final_javig.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto_final_javig.model.Lista
import com.example.proyecto_final_javig.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay

@Composable
fun UsersAdmin(userId: String) {
    val firestore = FirebaseFirestore.getInstance()

    val usuario = remember { mutableStateOf<User?>(null) }
    val listas = remember { mutableStateListOf<Lista>() }
    val productosPorLista = remember { mutableStateMapOf<String, List<Producto>>() }
    val expandedListas = remember { mutableStateMapOf<String, Boolean>() }

    // Cargar usuario
    LaunchedEffect(userId) {
        firestore.collection("users")
            .whereEqualTo("user_id", userId)
            .get()
            .addOnSuccessListener { snap ->
                val user = snap.documents.firstOrNull()?.toObject(User::class.java)
                usuario.value = user
            }
    }

    // Cargar listas
    LaunchedEffect(usuario.value) {
        val user = usuario.value ?: return@LaunchedEffect

        firestore.collection("listas")
            .get()
            .addOnSuccessListener { snap ->
                val todas = snap.documents.mapNotNull { it.toObject(Lista::class.java) }
                val propias = todas.filter { it.id_user == userId }
                val compartidas = todas.filter { it.id_compartir in user.id_compartir }

                listas.clear()
                listas.addAll(propias + compartidas)
            }
    }

    Column(Modifier.padding(16.dp)) {
        usuario.value?.let { user ->
            Text("📧 Email: ${user.email}")
            Text("🆔 UID: ${user.user_id}")
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn {
            items(listas, key = { it.id_lista }) { lista ->
                var nombreEditado by remember { mutableStateOf(lista.nombre_lista) }
                val expanded = expandedListas[lista.id_lista] == true
                var showConfirmDelete by remember { mutableStateOf(false) }
                var showNombreActualizado by remember { mutableStateOf(false) }

                // Efecto que oculta el mensaje después de 3 segundos
                LaunchedEffect(showNombreActualizado) {
                    if (showNombreActualizado) {
                        delay(3000)
                        showNombreActualizado = false
                    }
                }

                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .border(1.dp, Color.Gray)
                        .padding(8.dp)
                ) {
                    // Nombre editable
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = nombreEditado,
                            onValueChange = { nombreEditado = it },
                            label = { Text("Nombre de la lista") },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(Modifier.width(8.dp))
                        Button(onClick = {
                            firestore.collection("listas")
                                .whereEqualTo("id_lista", lista.id_lista)
                                .get()
                                .addOnSuccessListener {
                                    it.documents.firstOrNull()?.reference?.update("nombre_lista", nombreEditado)
                                    showNombreActualizado = true
                                }
                        }) {
                            Text("💾")
                        }
                        Spacer(Modifier.width(8.dp))
                        Button(
                            onClick = { showConfirmDelete = true },
                            colors = ButtonDefaults.buttonColors(Color.Red)
                        ) {
                            Text("🗑️")
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    // Botón Ver/Ocultar productos + mensaje de éxito
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Button(onClick = {
                            val nuevoEstado = !expanded
                            expandedListas[lista.id_lista] = nuevoEstado

                            if (nuevoEstado && !productosPorLista.containsKey(lista.id_lista)) {
                                firestore.collection("productos")
                                    .whereEqualTo("id_lista", lista.id_lista)
                                    .get()
                                    .addOnSuccessListener { snap ->
                                        val productos = snap.documents.map { doc ->
                                            val p = doc.toObject(Producto::class.java)
                                            p?.copy(documentId = doc.id)
                                        }.filterNotNull()
                                        productosPorLista[lista.id_lista] = productos
                                    }
                            }
                        }) {
                            Text(if (expanded) "Ocultar productos" else "Ver productos")
                        }

                        if (showNombreActualizado) {
                            Spacer(Modifier.width(8.dp))
                            Text("✅ Nombre actualizado", color = Color(0xFF4CAF50), fontSize = 14.sp)
                        }
                    }

                    if (expanded) {
                        productosPorLista[lista.id_lista]?.forEach { producto ->
                            ProductoItem(
                                producto = producto,
                                listaId = lista.id_lista,
                                onRefrescar = {
                                    firestore.collection("productos")
                                        .whereEqualTo("id_lista", lista.id_lista)
                                        .get()
                                        .addOnSuccessListener { snap ->
                                            val productos = snap.documents.map { doc ->
                                                val p = doc.toObject(Producto::class.java)
                                                p?.copy(documentId = doc.id)
                                            }.filterNotNull()
                                            productosPorLista[lista.id_lista] = productos
                                        }
                                }
                            )
                        }
                    }

                    // Diálogo para confirmar borrado
                    if (showConfirmDelete) {
                        AlertDialog(
                            onDismissRequest = { showConfirmDelete = false },
                            title = { Text("¿Eliminar lista?") },
                            text = { Text("Esta acción eliminará la lista y sus productos. ¿Quieres continuar?") },
                            confirmButton = {
                                TextButton(onClick = {
                                    firestore.collection("listas")
                                        .whereEqualTo("id_lista", lista.id_lista)
                                        .get()
                                        .addOnSuccessListener {
                                            it.documents.firstOrNull()?.reference?.delete()
                                        }
                                    showConfirmDelete = false
                                }) {
                                    Text("Eliminar", color = Color.Red)
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showConfirmDelete = false }) {
                                    Text("Cancelar")
                                }
                            }
                        )
                    }
                }
            }

        }
    }
}


@Composable
fun ProductoItem(
    producto: Producto,
    listaId: String,
    onRefrescar: () -> Unit
) {
    val firestore = FirebaseFirestore.getInstance()
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.LightGray)
            .padding(8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = producto.nombre_producto)
            Row {
                IconButton(onClick = { showEditDialog = true }) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                }
                IconButton(onClick = { showDeleteDialog = true }) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                }
            }
        }
    }

    // ✏️ Diálogo de edición
    if (showEditDialog) {
        var nuevoNombre by remember { mutableStateOf(producto.nombre_producto) }

        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Editar producto") },
            text = {
                OutlinedTextField(
                    value = nuevoNombre,
                    onValueChange = { nuevoNombre = it },
                    label = { Text("Nombre del producto") }
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    firestore.collection("productos")
                        .document(producto.documentId)
                        .update("nombre_producto", nuevoNombre)
                        .addOnSuccessListener {
                            showEditDialog = false
                            onRefrescar()
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

    // 🗑️ Diálogo de confirmación de borrado
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar producto") },
            text = { Text("¿Estás seguro de que quieres eliminar este producto?") },
            confirmButton = {
                TextButton(onClick = {
                    firestore.collection("productos")
                        .document(producto.documentId)
                        .delete()
                        .addOnSuccessListener {
                            showDeleteDialog = false
                            onRefrescar()
                        }
                }) {
                    Text("Eliminar", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
