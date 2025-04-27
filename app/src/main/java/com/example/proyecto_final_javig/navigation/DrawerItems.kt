package com.example.proyecto_final_javig.navigation

import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerItems(
    label: String,
    selected: Boolean,
    icon: ImageVector,
    contentDescription: String,
    route: String,
    navigationController: NavController,
    coroutineScope: CoroutineScope,
    drawerState: androidx.compose.material3.DrawerState
) {
    NavigationDrawerItem(
        label = { Text(text = label) },
        selected = selected,
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription
            )
        },
        onClick = {
            coroutineScope.launch {
                drawerState.close()
            }

            navigationController.navigate(route){
                //popUpTo(0)
                launchSingleTop = true
                restoreState = true
                popUpTo(navigationController.graph.startDestinationId) {
                    saveState = true
                }
            }
        }
    )
}