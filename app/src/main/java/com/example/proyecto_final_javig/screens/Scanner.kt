package com.example.proyecto_final_javig.screens

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.proyecto_final_javig.properties.CaptureActivityPortrait
import com.example.proyecto_final_javig.properties.DrawerContent
import com.example.proyecto_final_javig.properties.TopBarCustom
import com.example.proyecto_final_javig.ui.theme.colorAlpha1
import com.example.proyecto_final_javig.ui.theme.colorAlpha2
import com.example.proyecto_final_javig.ui.theme.colorBoton
import com.example.proyecto_final_javig.ui.theme.colorFondo
import com.example.proyecto_final_javig.ui.theme.colorTexto
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


// API Service
interface OpenFoodFactsApi {
    @GET("api/v0/product/{barcode}.json")
    suspend fun getProduct(@Path("barcode") barcode: String): ProductResponse
}

// Data models
data class ProductResponse(val product: Product?)
data class Product(
    val product_name: String?,
    val brands: String?,
    val stores: String?,
    val price: String?,
    val categories: String?,
    val labels: String?,
    val allergens: String?,
    val ingredients_text: String?
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Scanner(navController: NavHostController) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(
        color = colorAlpha2.value.copy(.2f).compositeOver(colorFondo.value), darkIcons = true
    )
    // Usamos ModalNavigationDrawer + Scaffold(topBar = TopBarCustom):
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            DrawerContent(
                navController = navController,
                drawerState = drawerState,
                scope = scope
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopBarCustom(
                    titulo = "Lector",
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            }
        ) { innerPadding ->
            // Aplicamos padding para no quedar debajo de la TopBar
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                ScannerContent()
            }
        }
    }
}


@Composable
fun ScannerContent() {
    var resultadoEscaner by remember { mutableStateOf("") }
    //API
    var producto by remember { mutableStateOf<Product?>(null) }

    val scanLauncher = rememberLauncherForActivityResult(
        contract = ScanContract(),
        onResult = { result ->
            resultadoEscaner = result.contents ?: "No hay resultados"
            //API
            obtenerDatosProducto(resultadoEscaner) { producto = it }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorFondo.value)
            .padding(
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            )
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1) Botón de escaneo con icono
        Button(
            onClick = {
                val options = ScanOptions().apply {
                    setBeepEnabled(true)
                    setCaptureActivity(CaptureActivityPortrait::class.java)
                    setOrientationLocked(false)
                }
                scanLauncher.launch(options)
            },
            colors = ButtonDefaults.buttonColors(colorBoton.value),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),

            ) {
            Icon(
                imageVector = Icons.Default.Camera,
                contentDescription = "Escanear",
                modifier = Modifier.size(20.dp),
                tint = colorTexto.value
            )
            Spacer(Modifier.width(8.dp))
            Text(
                "Escanear producto",
                style = TextStyle(
                    color = colorTexto.value,
                    fontSize = 16.sp,
                    fontWeight = Bold
                )
            )
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            tonalElevation = 4.dp,
            color = colorAlpha1.value.copy(.3f),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                if (producto == null) {
                    Text(
                        "Escanea un producto para ver sus datos",
                        style = MaterialTheme.typography.bodyLarge,
                        color = colorAlpha2.value.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    // Nombre y marca
                    Text(
                        text = producto!!.product_name.orEmpty(),
                        color = colorAlpha2.value.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = producto!!.brands.orEmpty(),
                        style = MaterialTheme.typography.bodyLarge,
                        color = colorAlpha2.value.copy(alpha = 0.8f),
                    )

                    Spacer(Modifier.height(12.dp))

                    // Ingredientes
                    Text(
                        "Ingredientes",
                        style = MaterialTheme.typography.titleLarge,
                        color = colorAlpha2.value.copy(alpha = 0.8f),

                        )

                    Text(
                        producto!!.ingredients_text.orEmpty(),
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.bodyLarge,
                        color = colorAlpha2.value.copy(alpha = 0.8f),

                        )

                    Spacer(Modifier.height(8.dp))

                    // Alérgenos
                    Text(
                        "Alérgenos",
                        style = MaterialTheme.typography.titleLarge,
                        color = colorAlpha2.value.copy(alpha = 0.8f),

                        )
                    Text(
                        producto!!.allergens.orEmpty().ifBlank { "No contiene información" },
                        style = MaterialTheme.typography.bodyLarge,
                        color = colorAlpha2.value.copy(alpha = 0.8f),

                        )

                    Spacer(Modifier.height(8.dp))

                    // Supermercados
                    Text(
                        "Disponible en",
                        style = MaterialTheme.typography.titleLarge,
                        color = colorAlpha2.value.copy(alpha = 0.8f),

                        )
                    Text(
                        producto!!.stores.orEmpty(),
                        style = MaterialTheme.typography.bodyLarge,
                        color = colorAlpha2.value.copy(alpha = 0.8f),

                        )
                }
            }
        }
    }
}

fun obtenerDatosProducto(codigo: String, onResult: (Product?) -> Unit) {
    if (codigo.isBlank()) {
        Log.e("API_ERROR", "Código de barras vacío o inválido")
        onResult(null)
        return
    }

    val retrofit = Retrofit.Builder()
        .baseUrl("https://world.openfoodfacts.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api = retrofit.create(OpenFoodFactsApi::class.java)
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = api.getProduct(codigo)
            withContext(Dispatchers.Main) {
                if (response.product != null) {
                    onResult(response.product)
                } else {
                    Log.e("API_ERROR", "Producto no encontrado para código: $codigo")
                    onResult(null)
                }
            }
        } catch (e: Exception) {
            Log.e("API_ERROR", "Error al obtener datos del producto: ${e.message}", e)
            withContext(Dispatchers.Main) {
                onResult(null)
            }
        }
    }
}