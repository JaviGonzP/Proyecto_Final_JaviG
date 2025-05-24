package com.example.proyecto_final_javig.model


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class LoginScreenViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val _loading = MutableLiveData(false)
    var userIncorr = false

    fun signInWithEmailAndPassword(email: String, password: String, onNavigate: (Boolean) -> Unit) =
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val userId = auth.currentUser?.uid ?: return@addOnCompleteListener

                            FirebaseFirestore.getInstance()
                                .collection("users")
                                .whereEqualTo("user_id", userId)
                                .get()
                                .addOnSuccessListener { documents ->
                                    val isAdmin = documents.firstOrNull()
                                        ?.getBoolean("admin") ?: false

                                    onNavigate(isAdmin)
                                }
                                .addOnFailureListener {
                                    Log.d("Showboxd", "Error obteniendo datos del usuario")
                                    onNavigate(false) // Por defecto ir usuario normal
                                }

                        } else {
                            val exception = task.exception
                            if (exception is FirebaseAuthInvalidCredentialsException) {
                                // Mostrar diálogo de error
                                userIncorr = true
                                // Manejar credenciales incorrectas
                                Log.d("Showboxd", "Credenciales incorrectas")
                                // Mostrar un mensaje de error al usuario
                                _loading.value = false
                                // Otras acciones para manejar el error
                            } else {
                                // Manejar otros errores
                                Log.d("Showboxd", "Error al iniciar sesión: ${exception?.message}")
                            }
                        }
                    }
            } catch (ex: Exception) {
                Log.d("Showboxd", "signInWithEmailAndPassword ${ex.message}")
                // Manejar otros errores
            }
        }


    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        nombre: String,
        apellido: String,
        home: () -> Unit
    ) {
        if (_loading.value == false) {
            _loading.value = true
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val nombre = nombre
                        val email = email
                        val apellido = apellido
                        createUser(nombre, apellido, email)
                        home()
                    } else {
                        Log.d(
                            "Showboxd",
                            "createUserWithEmailAndPassword: ${task.result.toString()}"
                        )
                    }
                    _loading.value = false
                }
        }
    }


    private fun createUser(nombre: String?, apellido: String?, email: String?) {
        val userId = auth.currentUser?.uid

        //User data class
        val user = User(
            user_id = userId.toString(),
            email = email.toString(),
            nombre = nombre.toString(),
            apellido = apellido.toString(),
            avatarUrl = "https://cdn.icon-icons.com/icons2/3946/PNG/512/user_icon_250929.png",
            id_compartir = emptyList(),
            admin = false
        ).toMap()

        FirebaseFirestore.getInstance().collection("users")
            .add(user)
            .addOnSuccessListener {
                Log.d("Showboxd", "${it.id} created")
            }.addOnFailureListener {
                Log.d("Showboxd", "Ocurrio un error")
            }
    }
}