package com.example.proyecto_final_javig.model

data class ListaItems(
    var id_compartir: String = "",
    val id_lista: String = "",
    val id_user: String = "",
    val nombre_lista: String = "",
    val nombre_sitio: String = ""
)

data class ProductosItems(
    val id_lista: String = "",
    val nombre_producto: String = "",
    val cantidad_producto: String = ""
)