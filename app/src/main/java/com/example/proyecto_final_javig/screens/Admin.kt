package com.example.proyecto_final_javig.screens

import android.util.Log
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
import com.google.firebase.functions.FirebaseFunctions

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
        //key para evitar que los usuarios se mezclen
        items(users, key = { it.user_id}){ user ->
            UserItem(user,
                onDelete = {
                    deleteUser(user)
                    users.remove(user)
                           },
                onEdit = { updatedFields -> updateUser(user.user_id, updatedFields)},
                onViewLists = {
                    navController.navigate("usersAdmin/${user.user_id}")
                }
            )
        }
    }

}

@Composable
fun UserItem(
    user: User,
    onDelete: () -> Unit,
    onEdit: (Map<String, Any>) -> Unit,
    onViewLists: () -> Unit
    ){
    var nombre by remember { mutableStateOf(user.nombre)}
    var apellido by remember { mutableStateOf(user.apellido) }

    Column(Modifier.padding(16.dp)) {
        Text("Email: ${user.email}")
        OutlinedTextField(value = nombre, onValueChange = { nombre = it}, label = { Text("Nombre") })
        OutlinedTextField(value = apellido, onValueChange = { apellido = it }, label = { Text("Apellido") })
    }

    Row {
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = {
            onEdit(mapOf("nombre" to nombre, "apellido" to apellido))
        }){
            Text("Guardar cambios")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = { onDelete() }, colors = ButtonDefaults.buttonColors(Color.Red)){
            Text("Eliminar", color = Color.White)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = onViewLists) {
            Text("Ver listas")
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

//fun deleteUser(user: User){
//    val db = FirebaseFirestore.getInstance()
//    val functions = FirebaseFunctions.getInstance()
//
//    db.collection("users")
//        .whereEqualTo("user_id", user.user_id)
//        .get()
//        .addOnSuccessListener { snapshot ->
//            snapshot.documents.firstOrNull()?.reference?.delete()
//
//            val data = hashMapOf("uid" to user.user_id)
//            functions
//                .getHttpsCallable("deleteUser")
//                .call(data)
//                .addOnSuccessListener {
//                    Log.d("Admin", "Usuario eliminado del Auth correctamente")
//                }
//                .addOnFailureListener { e ->
//                    Log.e("Admin", "Error eliminando usuario del Auth", e)
//                }
//        }
//        .addOnFailureListener { e ->
//            Log.e("Admin", "Error eliminando usuario de Firestore", e)
//        }
//}

data class DeleteUserRequest(val uid: String)

fun deleteUser(user: User) {
    val uid = user.user_id ?: return
    val request = DeleteUserRequest(uid)

    FirebaseFunctions.getInstance()
        .getHttpsCallable("deleteUser")
        .call(request)
        .addOnSuccessListener { result ->
            val res = result.data as Map<*, *>
            Log.d("Admin", "✅ Función ejecutada: ${res["message"]}")
        }
        .addOnFailureListener { e ->
            Log.e("Admin", "❌ Error al llamar a la función: ${e.message}")
        }
}
