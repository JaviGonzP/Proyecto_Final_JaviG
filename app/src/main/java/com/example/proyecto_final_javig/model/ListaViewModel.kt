package com.example.proyecto_final_javig.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore

class ListaViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val _loading = MutableLiveData(false)


    fun crearLista(
        nombreLista: String,
        nombreSitio: String,
        idLista: String
    ) {
        if (_loading.value == false) {
            _loading.value = true
            val userId = auth.currentUser?.uid ?: return

            val lista = mapOf(
                "nombre_lista" to nombreLista,
                "nombre_sitio" to nombreSitio,
                "id_lista" to idLista,
                "id_user" to userId,
                "id_compartir" to ""
            )

            FirebaseFirestore.getInstance().collection("listas")
                .add(lista)
                .addOnSuccessListener {
                    Log.d("Showboxd", "${it.id} created")
                }
                .addOnFailureListener {
                    Log.d("Showboxd", "Ocurrio un error")
                }
            _loading.value = false
        }
    }

    fun crearProducto(
        nombreProducto: String,
        cantidadProducto: String,
        idLista: String
    ) {
        if (_loading.value == false) {
            _loading.value = true
            val userId = auth.currentUser?.uid ?: return

            val lista = mapOf(
                "nombre_producto" to nombreProducto,
                "cantidad_producto" to cantidadProducto,
                "id_lista" to idLista,
                "id_user" to userId,
                "id_compartir" to ""
            )

            FirebaseFirestore.getInstance().collection("productos")
                .add(lista)
                .addOnSuccessListener {
                    Log.d("Showboxd", "${it.id} created")
                }
                .addOnFailureListener {
                    Log.d("Showboxd", "Ocurrio un error")
                }
            _loading.value = false
        }
    }

}