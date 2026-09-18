package com.example.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp

@Composable
fun HotelStudioBottomNav(
    activeModule: String,
    onTabSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Dashboard, contentDescription = null) },
            label = { 
                Text(
                    "Dashboard", 
                    fontSize = 10.sp, 
                    fontWeight = if (activeModule == "dashboard") FontWeight.Bold else FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                ) 
            },
            selected = activeModule == "dashboard",
            onClick = { onTabSelected("dashboard") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Restaurant, contentDescription = null) },
            label = { 
                Text(
                    "Buffet Tags", 
                    fontSize = 10.sp, 
                    fontWeight = if (activeModule == "buffet") FontWeight.Bold else FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                ) 
            },
            selected = activeModule == "buffet",
            onClick = { onTabSelected("buffet") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.RestaurantMenu, contentDescription = null) },
            label = { 
                Text(
                    "Menu Builder", 
                    fontSize = 10.sp, 
                    fontWeight = if (activeModule == "prospectus" || activeModule == "menu_builder") FontWeight.Bold else FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                ) 
            },
            selected = activeModule == "prospectus" || activeModule == "menu_builder",
            onClick = { onTabSelected("prospectus") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.MenuBook, contentDescription = null) },
            label = { 
                Text(
                    "Recipes", 
                    fontSize = 10.sp, 
                    fontWeight = if (activeModule == "recipes") FontWeight.Bold else FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                ) 
            },
            selected = activeModule == "recipes",
            onClick = { onTabSelected("recipes") }
        )
    }
}
