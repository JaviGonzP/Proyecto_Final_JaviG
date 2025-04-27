package com.example.proyecto_final_javig.model

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore

class DataViewModel : ViewModel() {
    private var listListener: ListenerRegistration? = null
    private var productListener: ListenerRegistration? = null
    private var userListener: ListenerRegistration? = null

    val stateM = mutableStateOf<List<ListaItems>>(emptyList())
    val stateP = mutableStateOf<List<ProductosItems>>(emptyList())
    val stateUs = mutableStateOf<List<User>>(emptyList())

    init {
        getData()
    }


    private fun getData() {
        listListener = FirebaseFirestore.getInstance().collection("listas")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val listItems = snapshot.toObjects(ListaItems::class.java)
                    stateM.value = listItems
                } else {
                    Log.d(TAG, "Current data: null")
                }
            }
        productListener = FirebaseFirestore.getInstance().collection("productos")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val productItems = snapshot.toObjects(ProductosItems::class.java)
                    stateP.value = productItems
                } else {
                    Log.d(TAG, "Current data: null")
                }
            }
        userListener = FirebaseFirestore.getInstance().collection("users")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val userItems = snapshot.toObjects(User::class.java)
                    stateUs.value = userItems.toMutableList()
                } else {
                    Log.d(TAG, "Current data: null")
                }
            }
    }

    override fun onCleared() {
        super.onCleared()
        listListener?.remove()
        productListener?.remove()
        userListener?.remove()
    }

    fun actualizarIdCompartir(id_lista: String, newIdCompartir: String) {
        FirebaseFirestore.getInstance().collection("listas")
            .whereEqualTo("id_lista", id_lista)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        document.reference.update("id_compartir", newIdCompartir)
                    }
                } else {
                    Log.d("Error", "Error getting documents: ", task.exception)
                }
            }
    }
}

fun deleteLista(id_lista: String) {
    com.google.firebase.Firebase.firestore.collection("listas").whereEqualTo("id_lista", id_lista)
        .get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    document.reference.delete()
                }
                com.google.firebase.Firebase.firestore.collection("productos")
                    .whereEqualTo("id_lista", id_lista).get().addOnCompleteListener { task2 ->
                        if (task2.isSuccessful) {
                            for (document2 in task2.result!!) {
                                document2.reference.delete()
                            }
                        } else {
                            Log.d("Error", "Error getting documents: ", task2.exception)
                        }
                    }
            } else {
                Log.d("Error", "Error getting documents: ", task.exception)
            }
        }
}

fun deleteProducto(nombre_producto: String) {
    com.google.firebase.Firebase.firestore.collection("productos")
        .whereEqualTo("nombre_producto", nombre_producto).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    document.reference.delete()
                }
            } else {
                Log.d("Error", "Error getting documents: ", task.exception)
            }
        }
}

fun editarProducto(nombre_producto: String) {
    com.google.firebase.Firebase.firestore.collection("productos")
        .whereEqualTo("nombre_producto", nombre_producto).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    document.reference.delete()
                }
            } else {
                Log.d("Error", "Error getting documents: ", task.exception)
            }
        }
}