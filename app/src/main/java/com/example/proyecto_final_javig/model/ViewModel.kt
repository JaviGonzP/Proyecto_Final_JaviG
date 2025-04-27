package com.example.proyecto_final_javig.model

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

data class Productos(
    val Producto: String = "",
    val Cantidad: String = "",
)

class ViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val _loading = MutableLiveData(false)

    private val firebaseFirestore = com.google.firebase.Firebase.firestore
    private val _productos = mutableStateOf<List<Productos>>(emptyList())

    init {
        getAllUserDocumentsVM()
    }

    fun getAllUserDocumentsVM() {
        val dRef = firebaseFirestore.collection("productos")

        dRef.get()
            .addOnSuccessListener { resultado ->
                _productos.value = resultado.toObjects()
            }
            .addOnFailureListener { excepcion ->
            }
    }
}
