package com.example.proyecto_final_javig.model

//Guardar datos en usuario
data class User(
    val user_id: String = "",
    val email: String = "",
    val nombre: String = "",
    val apellido: String = "",
    val avatarUrl: String = "",
    var id_compartir: List<String> = emptyList()
){
    fun toMap(): MutableMap<String, Any>{
        return mutableMapOf(
            "user_id" to this.user_id,
            "email" to this.email,
            "nombre" to this.nombre,
            "apellido" to this.apellido,
            "avatar_url" to this.avatarUrl,
            "id_compartir" to this.id_compartir
        )
    }
}
