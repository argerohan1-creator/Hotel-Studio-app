package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun HotelStudioTheme(
    activeTheme: String = "midnight",
    content: @Composable () -> Unit
) {
    val colorScheme = when (activeTheme.lowercase()) {
        "taj" -> TajGoldColorScheme
        "luxury" -> LuxuryObsidianColorScheme
        "ivory" -> FineDiningIvoryColorScheme
        "kitchen" -> KitchenHighContrastColorScheme
        "slate" -> MidnightDarkColorScheme
        "midnight" -> TajGoldColorScheme
        else -> TajGoldColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = HotelTypography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    HotelStudioTheme(activeTheme = "taj", content = content)
}
