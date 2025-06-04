package com.example.proyecto_final_javig.model

//Guardar datos en usuario
data class User(
    val user_id: String = "",
    val email: String = "",
    val nombre: String = "",
    val apellido: String = "",
    val avatarUrl: String = "",
    val tema: String = "",
    var id_compartir: List<String> = emptyList(),
    val admin: Boolean = false
){
    fun toMap(): MutableMap<String, Any>{
        return mutableMapOf(
            "user_id" to this.user_id,
            "email" to this.email,
            "nombre" to this.nombre,
            "apellido" to this.apellido,
            "avatar_url" to this.avatarUrl,
            "tema" to this.tema,
            "id_compartir" to this.id_compartir,
            "admin" to this.admin
        )
    }
}

data class Lista(
    val id_user: String = "",
    val id_compartir: String = "",
    val nombre_lista: String = "",
    val id_lista: String = ""
)