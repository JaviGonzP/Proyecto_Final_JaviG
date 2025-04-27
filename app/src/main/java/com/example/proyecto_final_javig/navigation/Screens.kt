package com.example.proyecto_final_javig.navigation

const val DETAIL_ARGUMENT_KEY = "id"

sealed class Screens(
    val route: String
) {
    object HomeContainer : Screens("home_container")
    object Principal : Screens("principal")
    object Inicio : Screens("inicio")
    object Ajustes : Screens("ajustes")
    object Lista : Screens("lista")
    object LogIn : Screens("logIn")
    object SignUp : Screens("signUp")
    object Mapa : Screens("mapa")
    object Contacto : Screens("contacto")
    object Scanner : Screens("scanner")

    //Pasar id en la card
    object InteriorLista : Screens("interior_lista/{$DETAIL_ARGUMENT_KEY}") {
        fun passId(id: String): String {
            return this.route.replace(oldValue = "{$DETAIL_ARGUMENT_KEY}", newValue = id.toString())
        }
    }
}
