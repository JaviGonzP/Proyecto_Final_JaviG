package com.example.proyecto_final_javig.properties

import com.example.proyecto_final_javig.ui.theme.boton_coral
import com.example.proyecto_final_javig.ui.theme.boton_default
import com.example.proyecto_final_javig.ui.theme.boton_verde
import com.example.proyecto_final_javig.ui.theme.colorBoton
import com.example.proyecto_final_javig.ui.theme.colorFondo
import com.example.proyecto_final_javig.ui.theme.fondo_coral
import com.example.proyecto_final_javig.ui.theme.fondo_default
import com.example.proyecto_final_javig.ui.theme.fondo_verde

fun SelectorDeColor(key: String) {
    when (key) {
        "color_1" -> {
            colorFondo.value = fondo_default
            colorBoton.value = boton_default
        }
        "color_2" -> {
            colorFondo.value = fondo_coral
            colorBoton.value = boton_coral
        }
        "color_3" -> {
            colorFondo.value = fondo_verde
            colorBoton.value = boton_verde
        }
        // añadir más casos según sea necesario
        else -> {
            colorFondo.value = fondo_default
            colorBoton.value = boton_default
        }
    }
}