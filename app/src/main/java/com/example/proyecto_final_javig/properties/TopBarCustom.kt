package com.example.proyecto_final_javig.properties

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyecto_final_javig.ui.theme.colorAlpha2

// 2) TopBarCustom.kt
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarCustom(
    titulo: String,
    onMenuClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        modifier = Modifier.height(60.dp).background(colorAlpha2.value.copy(alpha = .3f)),
        navigationIcon = {
            Box(
                modifier = Modifier.fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = onMenuClick,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(Icons.Default.Menu, contentDescription = "Menú")
                }
            }
        },
        title = {
            Box(
                modifier = Modifier.fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = titulo, maxLines = 1)
            }
        }
    )
}
