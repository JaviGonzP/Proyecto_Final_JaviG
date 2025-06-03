package com.example.proyecto_final_javig.properties

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.proyecto_final_javig.navigation.DrawerItems
import com.example.proyecto_final_javig.navigation.Items
import kotlinx.coroutines.CoroutineScope

// 1) DrawerContent.kt
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerContent(
    navController: NavHostController,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    ModalDrawerSheet {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Mi App",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))

        // 'Items.items' es tu lista de ítems para el Drawer
        Items.items.forEach { item ->
            DrawerItems(
                label = item.label,
                selected = (navController.currentBackStackEntryAsState().value?.destination?.route == item.route),
                icon = item.icon,
                contentDescription = item.contentDescription,
                route = item.route,
                coroutineScope = scope,
                drawerState = drawerState,
                navigationController = navController
            )
        }
    }
}
