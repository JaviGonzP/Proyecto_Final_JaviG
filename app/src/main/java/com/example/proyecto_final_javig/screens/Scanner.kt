package com.example.proyecto_final_javig.screens

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyecto_final_javig.properties.CaptureActivityPortrait
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


@Composable
fun Scanner(){
    var resultadoEscaner by remember { mutableStateOf("") }
    //API
    var producto by remember { mutableStateOf<Product?>(null) }

    val scanLauncher = rememberLauncherForActivityResult(
        contract = ScanContract(),
        onResult = {result ->
            resultadoEscaner = result.contents?: "No hay resultados"
            //API
            obtenerDatosProducto(resultadoEscaner) { producto = it }
        }
    )

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = "Resultado: ${resultadoEscaner}")
        Text(text = "${producto?.product_name ?: "Escanea un código"}")
        Text(text = "Marca: ${producto?.brands ?: "Desconocido"}")
        Text(text = "Precio: ${producto?.price ?: "No disponible"}")
        Text(text = "Tiendas: ${producto?.stores ?: "No disponible"}")
        Text(text = "Categoría: ${producto?.categories ?: "No disponible"}")
        Text(text = "Labels: ${producto?.labels ?: "No disponible"}")
        Text(text = "Alérgenos: ${producto?.allergens ?: "No disponible"}")
        Text(text = "Ingredientes: ${producto?.ingredients_text ?: "No disponible"}")

        Button(onClick = {
            val scanOptions = ScanOptions()
            scanOptions.setBeepEnabled(true)
            scanOptions.setCaptureActivity(CaptureActivityPortrait::class.java)
            scanOptions.setOrientationLocked(false)
            scanLauncher.launch(scanOptions)
        }) {
            Text(text = "Escanear")
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
                if (response?.product != null) {
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