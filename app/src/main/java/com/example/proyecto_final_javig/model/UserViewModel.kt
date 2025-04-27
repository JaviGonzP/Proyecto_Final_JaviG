package com.example.proyecto_final_javig.model

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    val stateU = mutableStateOf(User()) // Objeto de usuario
    private var userListener: ListenerRegistration? = null
    private var authStateListener: FirebaseAuth.AuthStateListener? = null

    init {
        getDataUser()
        addAuthStateListener()
    }

    private fun getDataUser() {
        viewModelScope.launch {
            // Obtener información del usuario actualmente autenticado
            val currentUser = auth.currentUser

            // Verificar si el usuario está autenticado
            if (currentUser != null) {
                // Obtener UID del usuario autenticado
                val uid = currentUser.uid
                listenToUserChanges(uid)
            }
        }
    }

    private fun listenToUserChanges(userId: String) {
        val db = FirebaseFirestore.getInstance()
        userListener = db.collection("users").whereEqualTo("user_id", userId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("listenToUserChanges", "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val user = snapshot.toObjects(User::class.java).firstOrNull()
                    if (user != null) {
                        stateU.value = user
                    } else {
                        Log.d(
                            "listenToUserChanges",
                            "Documento no encontrado para el usuario: $userId"
                        )
                    }
                } else {
                    Log.d("listenToUserChanges", "Current data: null")
                }
            }
    }

    private fun addAuthStateListener() {
        authStateListener = FirebaseAuth.AuthStateListener { auth ->
            val currentUser = auth.currentUser

            if (currentUser != null) {
                // Obtener UID del usuario autenticado
                val uid = currentUser.uid
                listenToUserChanges(uid)
            } else {
                // El usuario ha cerrado sesión
                stateU.value = User()
                userListener?.remove()
                userListener = null
            }
        }

        // Agregar el listener de estado de autenticación
        auth.addAuthStateListener(authStateListener!!)
    }

    override fun onCleared() {
        super.onCleared()

        // Remover el listener de estado de autenticación
        authStateListener?.let { auth.removeAuthStateListener(it) }
        authStateListener = null

        // Remover el listener de cambios en la colección de usuarios
        userListener?.remove()
        userListener = null
    }

    fun agregarIdCompartir(compartirId: String) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            FirebaseFirestore.getInstance().collection("users")
                .whereEqualTo("user_id", user.uid)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            document.reference.update(
                                "id_compartir",
                                FieldValue.arrayUnion(compartirId)
                            )
                        }
                    } else {
                        Log.d("Error", "Error getting documents: ", task.exception)
                    }
                }
        } else {

        }
    }

}