package com.example.proyecto_final_javig.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyecto_final_javig.model.User
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun Admin(navController: NavController) {
    val users = remember { mutableStateListOf<User>() }

    LaunchedEffect(Unit){
        FirebaseFirestore.getInstance()
            .collection("users")
            .get()
            .addOnSuccessListener { documents ->
                users.clear()
                for (document in documents){
                    val user = document.toObject(User::class.java)
                    users.add(user)
                }
            }
    }

    LazyColumn {
        items(users){ user ->
            UserItem(user,
                onDelete = { deleteUser(user) },
                onEdit = { updatedFields -> updateUser(user.user_id, updatedFields)}
            )
        }
    }

}

@Composable
fun UserItem(user: User, onDelete: () -> Unit, onEdit: (Map<String, Any>) -> Unit){
    var nombre by remember { mutableStateOf(user.nombre)}
    var apellido by remember { mutableStateOf(user.apellido) }

    Column(Modifier.padding(16.dp)) {
        Text("Email: ${user.email}")
        OutlinedTextField(value = nombre, onValueChange = { nombre = it}, label = { Text("Nombre") })
        OutlinedTextField(value = apellido, onValueChange = { apellido = it }, label = { Text("Apellido") })
    }

    Row {
        Button(onClick = {
            onEdit(mapOf("nombre" to nombre, "apellido" to apellido))
        }){
            Text("Guardar cambios")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = { onDelete() }, colors = ButtonDefaults.buttonColors(Color.Red)){
            Text("Eliminar")
        }
    }

}

fun updateUser(userId: String, updates: Map<String, Any>){
    val db = FirebaseFirestore.getInstance()
    db.collection("users")
        .whereEqualTo("user_id", userId)
        .get()
        .addOnSuccessListener { snapshot ->
            val doc = snapshot.documents.firstOrNull()
            doc?.reference?.update(updates)
        }
}

fun deleteUser(user: User){
    val db = FirebaseFirestore.getInstance()
    db.collection("users")
        .whereEqualTo("user_id", user.user_id)
        .get()
        .addOnSuccessListener { snapshot ->
            snapshot.documents.firstOrNull()?.reference?.delete()
        }
}