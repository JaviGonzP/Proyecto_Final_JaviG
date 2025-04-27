package com.example.proyecto_final_javig.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyecto_final_javig.model.ListaViewModel
import com.example.proyecto_final_javig.ui.theme.azul
import com.example.proyecto_final_javig.ui.theme.blanco
import com.example.proyecto_final_javig.ui.theme.fondo_azul
import com.example.proyecto_final_javig.ui.theme.fondo_rosa

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Inicio(navController: NavController) {
    //Barra(navController)
}

/*data class Producto(
    val producto: String,
    val cantidad: String
)

val productos = mutableStateListOf<Producto>()*/


@RequiresApi(Build.VERSION_CODES.O)
fun getFecha(): String {
    val now = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH:mm:ss")
    return now.format(formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Muestra(viewModel: ListaViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    //Valores para los campos de lista
    var nombre_lista_in by remember { mutableStateOf("") }
    var nombre_super_in by remember { mutableStateOf("") }
    var id_lista_fecha by remember { mutableStateOf("") }
    //Valores para los campos de productos
    var producto_in by remember { mutableStateOf("") }
    var cantidad_in by remember { mutableStateOf("") }
    // Variable para mostrar mensaje de error con campos vacios (vacio) y si ya hay un id lista guardado (lleno)
    var vacio by rememberSaveable { mutableStateOf(false) }
    var lleno by rememberSaveable { mutableStateOf(false) }
    var lista by rememberSaveable { mutableStateOf(false) }
    //Activar y desactivar textfields y botones
    var activado by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxHeight()
            .padding(top = 60.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        fondo_azul,
                        fondo_rosa
                    )
                )
            )
    ) {
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Row() {
                OutlinedTextField(
                    value = nombre_lista_in,
                    onValueChange = { nombre_lista_in = it },
                    label = { Text("Nombre de la lista") },
                    modifier = Modifier
                        .width(240.dp)
                        .padding(5.dp, 5.dp, 5.dp, 5.dp),
                    enabled = activado,
                    // .horizontalScroll(rememberScrollState()),
                    //maxLines = 1,
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White
                    )
                )
                OutlinedTextField(
                    value = nombre_super_in,
                    onValueChange = { nombre_super_in = it },
                    label = { Text("Local") },
                    modifier = Modifier
                        .width(235.dp)
                        .padding(5.dp, 5.dp, 5.dp, 5.dp),
                    enabled = activado,
                    // .horizontalScroll(rememberScrollState()),
                    // maxLines = 1,
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White
                    )
                )
            }
            Row() {
                Button(
                    onClick = {
                        if(activado == false){
                            viewModel.crearLista(nombre_lista_in, nombre_super_in, id_lista_fecha)
                        }
                        activado = true
                        nombre_lista_in = ""
                        nombre_super_in = ""
                        id_lista_fecha = ""
                        productos.clear()
                    }, modifier = Modifier.padding(5.dp),
                    colors = ButtonDefaults.buttonColors(blanco)
                ) {
                    Text(text = "Guardar lista")
                }

                Button(
                    onClick = {
                        // Comprobar si los campos están vacíos
                        if (nombre_lista_in.isNotEmpty() && nombre_super_in.isNotEmpty() && id_lista_fecha.isEmpty()) {
                            id_lista_fecha = getFecha()
                            //viewModel.crearLista(nombre_lista_in, nombre_super_in, id_lista_fecha)
                            activado = false
                        } else if (id_lista_fecha.isNotEmpty()) {
                            // Mostrar un diálogo de error
                            lleno = true;
                        } else {
                            vacio = true;
                        }
                    }, modifier = Modifier.padding(5.dp),
                    colors = ButtonDefaults.buttonColors(blanco),
                    enabled = activado
                ) {
                    Text(text = "Nueva lista")
                }
            }
            Row() {
                OutlinedTextField(
                    value = producto_in, onValueChange = { producto_in = it },
                    label = { Text("Producto") },
                    modifier = Modifier
                        .width(240.dp)
                        .padding(5.dp, 5.dp, 5.dp, 5.dp)
                        .horizontalScroll(rememberScrollState()),
                    maxLines = 1,
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White
                    )
                )
                OutlinedTextField(
                    value = cantidad_in,
                    onValueChange = { cantidad_in = it },
                    label = { Text("Cantidad") },
                    modifier = Modifier
                        .width(235.dp)
                        .padding(5.dp, 5.dp, 5.dp, 5.dp)
                        .horizontalScroll(rememberScrollState()),
                    maxLines = 1,
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White
                    )
                )
            }
            Row {
                Button(
                    onClick = {
                        // Comprobar si los campos están vacíos
                        if (producto_in.isNotEmpty() && cantidad_in.isNotEmpty() && id_lista_fecha.isNotEmpty()) {
                            viewModel.crearProducto(producto_in, cantidad_in, id_lista_fecha)
                            val nuevoProducto = Producto(producto_in, cantidad_in)
                            productos.add(nuevoProducto)
                            producto_in = ""
                            cantidad_in = ""

                        } else if (id_lista_fecha.isEmpty()) {
                            // Mostrar un diálogo de error
                            lista = true
                        } else {
                            // Mostrar un diálogo de error
                            vacio = true;
                        }
                    }, modifier = Modifier.padding(5.dp),
                    colors = ButtonDefaults.buttonColors(blanco)
                ) {
                    Text(text = "Agregar")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Column(modifier = Modifier.background(azul)) {
                Row(
                    modifier = Modifier.background(blanco),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 6.dp, bottom = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            color = Color.White,
                            text = "Productos agregados a $nombre_lista_in",
                            fontSize = 20.sp
                        )
                    }
                }
                LazyColumn() {
                    items(productos) { producto ->
                        MostrarProducto(producto)
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth(),
                            thickness = 3.dp,
                            color = Color.Black
                        )
                    }
                }
            }
            DialogVacio(vacio = vacio, onDismiss = { vacio = false })
            DialogLleno(lleno = lleno, onDismiss = { lleno = false })
            DialogLista(lista = lista, onDismiss = { lista = false })

        }
    }
}

@Composable
fun DialogVacio(
    vacio: Boolean,
    onDismiss: () -> Unit
) {
    if (vacio) {
        AlertDialog(onDismissRequest = { onDismiss() },
            confirmButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text(text = "Aceptar")
                }
            },
            title = { Text(text = "Error") },
            text = { Text(text = "Rellena los 2 campos") })
    }
}

@Composable
fun DialogLleno(
    lleno: Boolean,
    onDismiss: () -> Unit
) {
    if (lleno) {
        AlertDialog(onDismissRequest = { onDismiss() },
            confirmButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text(text = "Aceptar")
                }
            },
            title = { Text(text = "Error") },
            text = { Text(text = "Guarda la lista actual antes de crear una nueva") })
    }
}

@Composable
fun DialogLista(
    lista: Boolean,
    onDismiss: () -> Unit
) {
    if (lista) {
        AlertDialog(onDismissRequest = { onDismiss() },
            confirmButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text(text = "Aceptar")
                }
            },
            title = { Text(text = "Error") },
            text = { Text(text = "Tiene que haber una lista creada antes de añadirle productos") })
    }
}

@Composable
fun MostrarProducto(producto: Producto) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
    ) {
        Text(
            color = Color.Black,
            text = "${producto.cantidad} de ${producto.producto}",
            fontSize = 20.sp,
            modifier = Modifier
                .padding(top = 6.dp, bottom = 6.dp)
        )
    }
}

/*@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Barra(navController: NavController) {
    Scaffold(
        topBar = {
            var showMenu by remember {
                mutableStateOf(false)
            }
            TopAppBar(
                //Colores
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = morado,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSecondary
                ),
                // Título de la barra superior
                title = {
                    Text(
                        text = "Crear listas",
                        color = Color.White,
                        fontSize = 25.sp
                    )
                }
            )
        },

        ) {
        Muestra()
    }
}*/



