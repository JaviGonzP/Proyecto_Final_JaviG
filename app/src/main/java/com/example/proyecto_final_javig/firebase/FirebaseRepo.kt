package com.example.proyecto_final_javig.firebase

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseRepo {

    val db = FirebaseFirestore.getInstance()

    fun getAllUserDocuments(){
        /*com.example.javi_gonzalez_proyecto.screens.*/
        db.collection("productos").get().addOnSuccessListener { resultado->
            for(documento in resultado){
                val producto = documento.getString("producto")
                val cantidad = documento.getString("cantidad")
                Log.d("Info productos", "Producto: $producto / Cantidad: $cantidad")
            }
        }
    }
}